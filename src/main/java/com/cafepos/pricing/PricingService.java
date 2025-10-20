package com.cafepos.pricing;

import com.cafepos.common.money;
import java.math.BigDecimal;

public final class PricingService {
    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;

    public PricingService(DiscountPolicy discountPolicy, TaxPolicy taxPolicy) {
        this.discountPolicy = discountPolicy;
        this.taxPolicy = taxPolicy;
    }

    public PricingResult price(money subtotal) {
        money discount = discountPolicy.discountOf(subtotal);
        BigDecimal discountedValue = subtotal.amount().subtract(discount.amount());
        if (discountedValue.signum() < 0)
            discountedValue = BigDecimal.ZERO;
        money discounted = money.of(BigDecimal.valueOf(discountedValue.doubleValue()));
        money tax = taxPolicy.taxOn(discounted);
        money total = discounted.add(tax);
        return new PricingResult(subtotal, discount, tax, total);
    }

    public static record PricingResult(money subtotal, money discount, money tax, money total) {}
}
