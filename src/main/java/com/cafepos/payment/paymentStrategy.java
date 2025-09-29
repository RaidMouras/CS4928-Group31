package com.cafepos.payment;

import com.cafepos.domain.order;

public interface paymentStrategy { //List of variables and methods that we will use for the payment
    void pay(order order);
}
