package com.onlineshop.maxipetbackend.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private String id;
    private LocalDate date;
    private Double totalAmount;
    private String userId;
    private String address;
    private String phone;
    private String status;
    private String cardNumber;
    private String paymentMethod;
}
