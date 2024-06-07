package com.onlineshop.maxipetbackend.services.payment;

public class PaymentMethodFactory {
    public static PaymentMethod createPaymentMethod(String type, String cardNumber) {
        return switch (type) {
            case "CreditCard" -> new CreditCardPayment(cardNumber);
            case "CashOnDelivery" -> new CashOnDeliveryPayment();
            default -> throw new IllegalArgumentException("Unknown payment method type.");
        };
    }
}
