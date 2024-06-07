package com.onlineshop.maxipetbackend.dtos.mappers;

import com.onlineshop.maxipetbackend.dtos.ShoppingCartDTO;
import com.onlineshop.maxipetbackend.entities.ShoppingCart;

import java.util.stream.Collectors;

public class ShoppingCartMapper {
    public static ShoppingCartDTO toShoppingCartDTO(ShoppingCart shoppingCart) {
        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .totalAmount(shoppingCart.getTotalAmount())
                .userId(shoppingCart.getLocalUser().getId())
                .build();
    }

    public static ShoppingCart toShoppingCartEntity(ShoppingCartDTO shoppingCartDTO){
        return ShoppingCart.builder()
                .id(shoppingCartDTO.getId())
                .totalAmount(shoppingCartDTO.getTotalAmount())
                .build();
    }
}
