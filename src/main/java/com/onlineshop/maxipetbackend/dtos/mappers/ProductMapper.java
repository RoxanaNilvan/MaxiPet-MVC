package com.onlineshop.maxipetbackend.dtos.mappers;

import com.onlineshop.maxipetbackend.dtos.ProductDTO;
import com.onlineshop.maxipetbackend.entities.Product;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ProductMapper {
    public static ProductDTO toProductDTO(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoryName(product.getCategory().getName())
                .categoryAnimal(product.getCategory().getAnimal())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public static Product toEntity(ProductDTO productDTO){
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .imageUrl(productDTO.getImageUrl())
                .build();
    }
}
