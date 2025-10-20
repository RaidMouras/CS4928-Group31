package com.cafepos.pricing;

import com.cafepos.common.money;

public interface DiscountPolicy {
    money discountOf(money subtotal);
}
