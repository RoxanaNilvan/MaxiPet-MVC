package com.onlineshop.maxipetbackend.dtos.mappers;


import com.onlineshop.maxipetbackend.dtos.PromotionsDTO;
import com.onlineshop.maxipetbackend.entities.Promotions;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PromotionsMapper {
    public static PromotionsDTO toPromotionsDTO(Promotions promotions){
        return PromotionsDTO.builder()
                .id(promotions.getId())
                .discount(promotions.getDiscount())
                .endDate(promotions.getDiscountDate())
                .initialPrice(promotions.getInitialPrice())
                .finalPrice(promotions.getDiscountedPrice())
                .productName(promotions.getProduct().getName())
                .build();
    }

    public static Promotions toEntity(PromotionsDTO promotionsDTO){
        return Promotions.builder()
                .id(promotionsDTO.getId())
                .discount(promotionsDTO.getDiscount())
                .discountDate(promotionsDTO.getEndDate())
                .build();
    }
}
