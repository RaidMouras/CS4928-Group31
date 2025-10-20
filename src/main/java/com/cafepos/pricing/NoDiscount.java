package com.cafepos.pricing;

import com.cafepos.common.money;

public final class NoDiscount implements DiscountPolicy {
    @Override
    public money discountOf(money subtotal) {
        return money.zero();
    }
}
