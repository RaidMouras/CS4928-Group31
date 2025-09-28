package com.cafepos.payment;

import com.cafepos.domain.order;

public final class cashPayment implements paymentStrategy {
    @Override
    public void pay(order order) {
        System.out.println("[Cash] Customer paid " + order.totalWithTax(10) + " EUR");
    }
}
