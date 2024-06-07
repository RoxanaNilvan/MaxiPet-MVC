package com.onlineshop.maxipetbackend.dtos.mappers;


import com.onlineshop.maxipetbackend.dtos.ShoppingCartItemDTO;
import com.onlineshop.maxipetbackend.entities.ShoppingCartItem;

public class ShoppingCartItemMapper {
    public static ShoppingCartItemDTO toShoppingCartItemDTO(ShoppingCartItem shoppingCartItem) {
        return ShoppingCartItemDTO.builder()
                .id(shoppingCartItem.getId())
                .quantity(shoppingCartItem.getQuantity())
                .price(shoppingCartItem.getPrice())
                .shoppingCartId(shoppingCartItem.getShoppingCart().getId())
                .productId(shoppingCartItem.getProduct().getId())
                .productName(shoppingCartItem.getProduct().getName())
                .build();
    }

    public static ShoppingCartItem toShoppingCartItemEntity(ShoppingCartItemDTO shoppingCartItemDTO) {
        return ShoppingCartItem.builder()
                .id(shoppingCartItemDTO.getId())
                .quantity(shoppingCartItemDTO.getQuantity())
                .price(shoppingCartItemDTO.getPrice())
                .build();
    }
}
