package com.cafepos;

import com.cafepos.domain.order;
import com.cafepos.payment.paymentStrategy;

public class fakePaymentStrategy implements paymentStrategy {
    public boolean called = false;

    @Override
    public void pay(order order) {
        called = true;
    }
}
