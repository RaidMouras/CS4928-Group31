package com.cafepos.payment;

import com.cafepos.domain.order;

public interface paymentStrategy {
    void pay(order order);
}
