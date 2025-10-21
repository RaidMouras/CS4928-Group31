package com.cafepos.payment;

import com.cafepos.common.Money;

public interface paymentStrategy {
    void pay(Money total);
}
