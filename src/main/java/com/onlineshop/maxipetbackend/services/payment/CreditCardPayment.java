package com.onlineshop.maxipetbackend.services.payment;

public class CreditCardPayment implements PaymentMethod{
    private final String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String pay(double amount) {
        if(validateCardNumber(cardNumber)) {
            System.out.println("Paid " + amount + " using Credit Card: " + cardNumber);
            return "Paid " + amount + " lei using Credit Card: " + cardNumber;
        }else{
            return "Could not precess payment";
        }
    }

    private static boolean validateCardNumber(String cardNumber) {
        return cardNumber != null && cardNumber.matches("^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$");
    }
}
