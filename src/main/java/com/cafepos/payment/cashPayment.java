package com.cafepos.payment;

import com.cafepos.common.Money;

public final class cashPayment implements paymentStrategy {
    @Override
    public void pay(Money total) {
        System.out.println("[Cash] Customer paid " + total + "EUR");
    }
}
