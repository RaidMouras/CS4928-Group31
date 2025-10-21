package com.cafepos.payment;

import com.cafepos.common.Money;

public final class unknownPayment implements paymentStrategy {
    @Override
    public void pay(Money total) {
        System.out.println("[UnknownPayment] " + total);
    }
}
