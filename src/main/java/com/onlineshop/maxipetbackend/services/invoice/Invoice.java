package com.onlineshop.maxipetbackend.services.invoice;

import com.onlineshop.maxipetbackend.entities.OrderItems;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Invoice {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private List<OrderItems> orderItemsList;
    private double totalPrice;
}
