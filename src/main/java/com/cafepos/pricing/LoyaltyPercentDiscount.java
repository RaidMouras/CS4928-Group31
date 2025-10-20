package com.cafepos.pricing;

import com.cafepos.common.money;
import java.math.BigDecimal;

public final class LoyaltyPercentDiscount implements DiscountPolicy {
    private final int percent;
    public LoyaltyPercentDiscount(int percent) {
        if (percent <= 0) throw new IllegalArgumentException("percent must be positive");
        this.percent = percent;
    }

    @Override
    public money discountOf(money subtotal) {
        BigDecimal d = subtotal.asBigDecimal()
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100));
        return money.of(d);
    }
}
