package com.cafepos.payment;

import com.cafepos.domain.order;

public final class cardPayment implements paymentStrategy {
    private final String cardNumber;

    public cardPayment(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("card number required");
        }
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(order order) {
        String maskedCard = "****" + cardNumber.substring(cardNumber.length() - 4);
        System.out.println("[Card] Customer paid " + order.totalWithTax(10) + " EUR with card " + maskedCard);
    }
}