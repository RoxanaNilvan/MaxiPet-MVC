package com.onlineshop.maxipetbackend.dtos.mappers;

import com.onlineshop.maxipetbackend.dtos.CategoryDTO;
import com.onlineshop.maxipetbackend.entities.Category;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryMapper {
    public static CategoryDTO toCategoryDTO(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .animal(category.getAnimal())
                .build();
    }

    public static Category toEntity(CategoryDTO categoryDTO){
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .animal(categoryDTO.getAnimal())
                .build();
    }
}
