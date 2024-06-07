package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.constants.UserLogger;
import com.onlineshop.maxipetbackend.dtos.UserDTO;
import com.onlineshop.maxipetbackend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Retrieves a list of users.
     * @return a list of UserDTO objects representing the users
     */
    @GetMapping("/findAllUsers")
    public ModelAndView getUsers(){
        List<UserDTO> users = userService.findUsers();
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("users", users);
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Retrieves the user with the specified ID.
     * @param userId the ID of the user to retrieve
     * @return the UserDTO object representing the found user
     */
    @GetMapping("/findById/{id}")
    public ResponseEntity<UserDTO> findUser(@PathVariable("id") String userId){
        UserDTO userDTO = userService.findUserById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Inserts a new user.
     * @param userDTO the UserDTO object containing the information about the user to insert
     * @return the ID of the inserted user
     */
    @PostMapping("/insertUser")
    public ModelAndView insertUser(@Valid UserDTO userDTO){
        String userId = userService.insertUser(userDTO);
        ModelAndView mav = new ModelAndView();
        if(userId != null){
            mav.addObject("successInsertUser", UserLogger.USER_INSERTED);
        }else{
            mav.addObject("errorInsertUser", UserLogger.USER_INSERTED_FAILED);
        }
        mav.setViewName("redirect:/user/findAllUsers");
        mav.addObject("user", userDTO);
        return mav;
    }

    /**
     * Inserts a new client.
     * This method inserts a new client into the system using the provided UserDTO object.
     * @param userDTO the UserDTO object containing the information about the client to insert
     * @return a ModelAndView object to redirect to the home page after the insertion
     */
    @PostMapping("/insertClient")
    public ModelAndView insertClient(@Valid UserDTO userDTO){
        String userId = userService.insertClient(userDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/");
        return mav;
    }

    /**
     * Deletes a user.
     * @param userId the ID of the user to delete
     */
    @PostMapping ("/removeUser/{id}")
    public ModelAndView removeUser(@PathVariable("id") String userId){
        return deleteUser(userId);
    }

    /**
     * Deletes a user.
     * @param userId the ID of the user to delete
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String userId){
        int rez = userService.delete(userId);
        ModelAndView mav = new ModelAndView();
        if(rez == 0){
            mav.addObject("successDeleteUser", UserLogger.USER_DELETED);
        }else{
            mav.addObject("errorDeleteUser", UserLogger.USER_DELETION_FAILED);
        }
        mav.setViewName("redirect:/user/findAllUsers");
        return mav;
    }

    /**
     * Updates a user.
     * @param userId the ID of the user to update
     * @param userDTO the UserDTO object containing the new information for the user to update
     * @return the UserDTO object representing the updated user
     */
    @PostMapping ("/updateUser/{id}")
    public ModelAndView updateUserC(@PathVariable("id") String userId, UserDTO userDTO){
        return updateUser(userId, userDTO);
    }

    /**
     * Updates a user.
     * @param userId the ID of the user to update
     * @param userDTO the UserDTO object containing the new information for the user to update
     * @return the UserDTO object representing the updated user
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") String userId, UserDTO userDTO){
        UserDTO user = userService.update(userId, userDTO);
        ModelAndView mav = new ModelAndView();
        if (userId != null) {
            mav.addObject("successUpdateUser", UserLogger.USER_UPDATED);
        }else{
            mav.addObject("errorUpdateUser", UserLogger.USER_UPDATED_FAILED);
        }
        mav.setViewName("redirect:/user/findAllUsers");
        return mav;
    }
}
