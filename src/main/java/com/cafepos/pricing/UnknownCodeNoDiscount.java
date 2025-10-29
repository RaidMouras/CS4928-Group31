package com.cafepos.pricing;

import com.cafepos.common.Money; /** Unknown codes map to zero discount and no printed discount line, but retain the code if you still store it globally. */
public final class UnknownCodeNoDiscount implements DiscountPolicy {
    private final String unknown;
    UnknownCodeNoDiscount(String unknown) { this.unknown = unknown; }
    @Override
    public Money discount(Money subtotal) { return Money.zero(); }
    @Override
    public boolean printsLine(Money computedDiscount) { return false; }
    @Override
    public String code() { return unknown; }
}
