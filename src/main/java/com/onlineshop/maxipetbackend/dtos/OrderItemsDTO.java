package com.onlineshop.maxipetbackend.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemsDTO {
    private String id;
    private String orderId;
    private String productId;
    private int quantity;
    private Double price;
}
