package com.cafepos.pricing;

import com.cafepos.common.Money;
import java.math.BigDecimal;

public final class PercentTaxPolicy implements TaxPolicy {
    private final int percent;

    public PercentTaxPolicy(int percent) {
        this.percent = percent;
    }

    @Override
    public Money tax(Money discounted) {
        return Money.of(discounted.asBigDecimal()
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100)));
    }

    @Override
    public String label(int taxPercent) {
        // Keep exact formatting as in legacy receipt: "Tax (10%):"
        return "Tax (" + taxPercent + "%):";
    }

    public int percent() { return percent; }
}
