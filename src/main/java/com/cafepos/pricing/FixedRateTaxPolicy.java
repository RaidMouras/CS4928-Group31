package com.cafepos.pricing;

import com.cafepos.common.money;
import java.math.BigDecimal;

public final class FixedRateTaxPolicy implements TaxPolicy {
    private final int percent;
    public FixedRateTaxPolicy(int percent) {
        if (percent < 0) throw new IllegalArgumentException("percent must be non-negative");
        this.percent = percent;
    }

    @Override
    public money taxOn(money amount) {
        BigDecimal tax = amount.asBigDecimal()
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100));
        return money.of(tax);
    }

    public int percent() { return percent; }
}
