package com.onlineshop.maxipetbackend.validators;

import com.onlineshop.maxipetbackend.entities.LocalOrder;

import java.time.LocalDate;

public class OrderValidators {
    public static boolean validateOrder(LocalOrder order) {
        return validateDate(order.getDate()) &&
                validateTotalAmount(order.getTotalAmount()) &&
                validateAddress(order.getAddress()) &&
                validateStatus(order.getStatus()) &&
                validatePhone(order.getPhone());
    }

    private static boolean validateDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    private static boolean validateTotalAmount(Double totalAmount) {
        return totalAmount != null && totalAmount >= 0;
    }

    private static boolean validateAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    private static boolean validateStatus(String status) {
        return status != null && !status.trim().isEmpty();
    }

    private static boolean validatePhone(String phone) {
        return phone != null && phone.matches("^(\\+4|004)?07[0-9]{8}$");
    }
}
