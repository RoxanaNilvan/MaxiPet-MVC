package com.onlineshop.maxipetbackend.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDTO {
    private UUID id;
    private String name;
    private String address;
    private int age;
}
