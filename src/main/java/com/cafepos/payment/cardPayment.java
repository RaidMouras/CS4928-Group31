package com.cafepos.payment;

import com.cafepos.common.Money;
import com.cafepos.domain.Order;

public final class cardPayment implements paymentStrategy {
    private final String cardNumber;
    public cardPayment(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("card number required");
        }
        this.cardNumber = cardNumber;
    }
    @Override
    public void pay(Money total) {
        String lastFour = cardNumber.substring(cardNumber.length() - 4);  //turn the number into a string, and extract the last 4 digits
        String maskedCard = "****" + lastFour;
        System.out.println("[Card] Customer paid " + total + " EUR with card " + maskedCard);
    }
}
