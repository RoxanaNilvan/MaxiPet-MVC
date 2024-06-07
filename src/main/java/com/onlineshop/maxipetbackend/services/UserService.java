package com.onlineshop.maxipetbackend.services;

import com.onlineshop.maxipetbackend.config.RabbitSender;
import com.onlineshop.maxipetbackend.constants.RegisterLogger;
import com.onlineshop.maxipetbackend.constants.UserLogger;
import com.onlineshop.maxipetbackend.dtos.NotificationRequestDTO;
import com.onlineshop.maxipetbackend.dtos.UserDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.UserMapper;
import com.onlineshop.maxipetbackend.entities.LocalUser;
import com.onlineshop.maxipetbackend.entities.ShoppingCart;
import com.onlineshop.maxipetbackend.repositories.ShoppingCartRepository;
import com.onlineshop.maxipetbackend.repositories.UserRepository;
import com.onlineshop.maxipetbackend.validators.UserValidators;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final RabbitSender rabbitSender;


    /**
     * Returns a list of UserDTO objects representing the users.
     * @return A list of UserDTO objects
     */
    public List<UserDTO> findUsers(){
        List<LocalUser> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns the user with the specified ID.
     * @param id The ID of the user to find
     * @return The UserDTO object representing the found user
     */
    public UserDTO findUserById(String id){
        Optional<LocalUser> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) LOGGER.error(UserLogger.USER_WITH_ID_NOT_FOUND, id);
        return UserMapper.toUserDTO(userOptional.get());
    }

    /**
     * Method to find a user by email address and password.
     * @param email The email address of the user to find
     * @param password The password of the user to find
     * @return The object representing the found user, or null if not found
     */
    public UserDTO findUserByEmailAndPassword(String email, String password){
        LocalUser userOptional = userRepository.findLocalUserByEmailAndPassword(email, password);
        if(userOptional == null){
            LOGGER.error(UserLogger.USER_WITH_EMAIL_NOT_FOUND, email);
        }
        return UserMapper.toUserDTO(userOptional);
    }

    /**
     * Inserts a new user into the database.
     * @param userDTO The UserDTO object containing information about the user to be inserted
     * @return The ID of the inserted user
     */
    public String insertUser(UserDTO userDTO){
        LocalUser user = UserMapper.toEntity(userDTO);

        // Validate user
        if (!UserValidators.validateUser(user)) {
            LOGGER.error("Validation error: Invalid user data");
            return null;
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        user = userRepository.save(user);
        shoppingCart.setLocalUser(user);
        shoppingCartRepository.save(shoppingCart);
        user.setShoppingCart(shoppingCart);
        user = userRepository.save(user);
        LOGGER.debug(UserLogger.USER_WITH_ID_INSERTED, user.getId());
        return user.getId();
    }

    /**
     * Inserts a new client into the database.
     * @param userDTO The UserDTO object containing information about the user to be inserted
     * @return The ID of the inserted user
     */
    public String insertClient(UserDTO userDTO){
        LocalUser user = UserMapper.toEntity(userDTO);
        user.setRole("client");

        // Validate user
        if (!UserValidators.validateUser(user)) {
            LOGGER.error("Validation error: Invalid user data");
            return null;
        }

        user = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setLocalUser(user);
        shoppingCartRepository.save(shoppingCart);
        user.setShoppingCart(shoppingCart);
        user = userRepository.save(user);
        LOGGER.debug(UserLogger.USER_WITH_ID_INSERTED, user.getId());

        //send information to the queue
        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
        notificationRequestDTO.setEmail(user.getEmail());
        notificationRequestDTO.setName(user.getUserName());
        notificationRequestDTO.setSubject(RegisterLogger.registerSubject);
        notificationRequestDTO.setMessage(RegisterLogger.registerBody);
        rabbitSender.sendMessage(notificationRequestDTO);

        return user.getId();
    }

    /**
     * Deletes a user from the database.
     * @param id The ID of the user to delete
     * @return 0 if the user was successfully deleted, -1 otherwise
     */
    public int delete(String id){
        Optional<LocalUser> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            LOGGER.error(UserLogger.USER_WITH_ID_NOT_FOUND, id);
            return -1;
        }else {
            userRepository.deleteById(id);
            LOGGER.debug(UserLogger.USER_WITH_ID_DELETED, id);
            return 0;
        }
    }

    /**
     * Updates a user in the database.
     * @param id The ID of the user to update
     * @param userDTO The UserDTO object containing the new information for the user to be updated
     * @return The UserDTO object representing the updated user
     */
    public UserDTO update(String id, UserDTO userDTO) {
        Optional<LocalUser> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error(UserLogger.USER_WITH_ID_NOT_FOUND, id);
        } else {
            LocalUser existingUser = userOptional.get();
            if (userDTO.getUserName() != null) {
                existingUser.setUserName(userDTO.getUserName());
            }
            if (userDTO.getEmail() != null) {
                existingUser.setEmail(userDTO.getEmail());
            }
            userRepository.save(existingUser);
            LOGGER.debug(UserLogger.USER_WITH_ID_UPDATED, id);
            return UserMapper.toUserDTO(existingUser);
        }
        return userDTO;
    }
}
