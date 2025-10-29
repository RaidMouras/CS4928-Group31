package com.cafepos.pricing;

import com.cafepos.common.Money; /** Fixed 1.00 coupon (for "COUPON1"). */
public final class CouponFixed1 implements DiscountPolicy {
    @Override
    public Money discount(Money subtotal) { return Money.of(1.00); }
    @Override
    public boolean printsLine(Money computedDiscount) { return computedDiscount.asBigDecimal().signum() > 0; }
    @Override
    public String code() { return "COUPON1"; }
}
