package com.onlineshop.maxipetbackend.dtos;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionsDTO {
    private String id;
    private int discount;
    private String productName;
    private LocalDate endDate;
    private double initialPrice;
    private double finalPrice;
}
