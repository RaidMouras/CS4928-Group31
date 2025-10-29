package com.cafepos.pricing;

import com.cafepos.common.Money;

import java.math.BigDecimal; /** 5% loyalty discount (for "LOYAL5"). */
public final class Loyalty5Percent implements DiscountPolicy {
    @Override
    public Money discount(Money subtotal) {
        return Money.of(subtotal.asBigDecimal()
                .multiply(BigDecimal.valueOf(5))
                .divide(BigDecimal.valueOf(100)));
    }
    @Override
    public boolean printsLine(Money computedDiscount) { return computedDiscount.asBigDecimal().signum() > 0; }
    @Override
    public String code() { return "LOYAL5"; }
}
