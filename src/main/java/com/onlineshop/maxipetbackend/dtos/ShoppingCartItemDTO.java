package com.onlineshop.maxipetbackend.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartItemDTO {
    private String id;
    private String shoppingCartId;
    private String productId;
    private int quantity;
    private Double price;
    private String productName;
}
