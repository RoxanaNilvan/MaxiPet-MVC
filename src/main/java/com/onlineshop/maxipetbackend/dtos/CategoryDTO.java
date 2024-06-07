package com.onlineshop.maxipetbackend.dtos;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryDTO {
    private String id;
    private String name;
    private String animal;
}
