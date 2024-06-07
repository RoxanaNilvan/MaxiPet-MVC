package com.onlineshop.maxipetbackend.dtos.mappers;

import com.onlineshop.maxipetbackend.dtos.UserDTO;
import com.onlineshop.maxipetbackend.entities.LocalUser;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserMapper {
    public static UserDTO toUserDTO(LocalUser user){
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public static LocalUser toEntity(UserDTO userDTO){
        return LocalUser.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .userName(userDTO.getUserName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
    }
}
