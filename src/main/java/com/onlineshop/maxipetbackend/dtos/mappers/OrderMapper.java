package com.onlineshop.maxipetbackend.dtos.mappers;

import com.onlineshop.maxipetbackend.dtos.OrderDTO;
import com.onlineshop.maxipetbackend.entities.LocalOrder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderMapper {
    public static OrderDTO toOrderDTO(LocalOrder order){
        return OrderDTO.builder()
                .id(order.getId())
                .date(order.getDate())
                .totalAmount(order.getTotalAmount())
                .userId(order.getUser().getId())
                .address(order.getAddress())
                .phone(order.getPhone())
                .status(order.getStatus())
                .cardNumber(order.getCardNumber())
                .paymentMethod(order.getPaymentMethod())
                .build();
    }

    public static LocalOrder toEntity(OrderDTO orderDTO){
        return LocalOrder.builder()
                .id(orderDTO.getId())
                .date(orderDTO.getDate())
                .totalAmount(orderDTO.getTotalAmount())
                .status(orderDTO.getStatus())
                .address(orderDTO.getAddress())
                .phone(orderDTO.getPhone())
                .cardNumber(orderDTO.getCardNumber())
                .paymentMethod(orderDTO.getPaymentMethod())
                .build();
    }
}
