package com.cafepos.pricing;

import com.cafepos.common.money;

public final class FixedCouponDiscount implements DiscountPolicy {
    private final money amount;
    public FixedCouponDiscount(money amount) { this.amount = amount; }

    @Override
    public money discountOf(money subtotal) {
        if (amount.asBigDecimal().compareTo(subtotal.asBigDecimal()) > 0)
            return subtotal;
        return amount;
    }
}
