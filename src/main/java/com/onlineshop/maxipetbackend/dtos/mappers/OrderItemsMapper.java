package com.onlineshop.maxipetbackend.dtos.mappers;


import com.onlineshop.maxipetbackend.dtos.OrderItemsDTO;
import com.onlineshop.maxipetbackend.entities.OrderItems;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderItemsMapper {

    public static OrderItemsDTO toOrderItemsDTO(OrderItems orderItems){
        return OrderItemsDTO.builder()
                .id(orderItems.getId())
                .quantity(orderItems.getQuantity())
                .price(orderItems.getPrice())
                .orderId(orderItems.getLocalOrder().getId())
                .productId(orderItems.getProduct().getId())
                .build();
    }

    public static OrderItems toEntity(OrderItemsDTO orderItemsDTO){
        return OrderItems.builder()
                .id(orderItemsDTO.getId())
                .quantity(orderItemsDTO.getQuantity())
                .price(orderItemsDTO.getPrice())
                .build();
    }
}
