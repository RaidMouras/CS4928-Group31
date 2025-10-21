package com.cafepos.payment;

import com.cafepos.common.Money;

public final class cardPayment implements paymentStrategy {
    @Override
    public void pay(Money total) {
        System.out.println("[Card] Customer paid " + total + "EUR with card ****1234");
    }
}
