package com.cafepos.pricing;

import com.cafepos.common.Money; /** No discount (used for "NONE" or when discountCode == null but you still want an object). */
public final class NoDiscount implements DiscountPolicy {
    @Override
    public Money discount(Money subtotal) { return Money.zero(); }
    @Override
    public boolean printsLine(Money computedDiscount) { return false; } // matches current behavior: only print when > 0
    @Override
    public String code() { return "NONE"; }
}
