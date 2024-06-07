package com.onlineshop.maxipetbackend.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String role;
}
