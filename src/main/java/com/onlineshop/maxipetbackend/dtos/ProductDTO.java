package com.onlineshop.maxipetbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDTO {
    private String id;

    @NotBlank(message = "Numele produsului nu poate fi gol")
    private String name;

    @PositiveOrZero(message = "Prețul trebuie să fie un număr pozitiv sau zero")
    private double price;

    @PositiveOrZero(message = "Stocul trebuie să fie un număr pozitiv sau zero")
    private int stock;

    @NotBlank(message = "Numele categorie nu poate fi gol")
    private String categoryName;

    @NotBlank(message = "Tipul animalului nu poate fi gol")
    private String categoryAnimal;

    private String imageUrl;
}
