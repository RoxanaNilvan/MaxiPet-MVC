package com.onlineshop.maxipetbackend.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ShoppingCartDTO {
    private String id;
    private Double totalAmount;
    private String userId;
}
