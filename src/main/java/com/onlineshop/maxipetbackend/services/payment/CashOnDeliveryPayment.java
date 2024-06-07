package com.onlineshop.maxipetbackend.services.payment;

public class CashOnDeliveryPayment implements PaymentMethod{
    @Override
    public String pay(double amount) {

        System.out.println("Payment of " + amount + " will be made in cash upon delivery.");
        return "Payment of " + amount + " lei will be made in cash upon delivery.";
    }
}
