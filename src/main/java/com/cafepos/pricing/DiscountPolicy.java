package com.cafepos.pricing;

import com.cafepos.common.Money;
import java.math.BigDecimal;

/**
 * Computes discount from a subtotal and exposes whether a receipt line should be printed.
 * Implementations mirror existing codes: NONE, LOYAL5 (5%), COUPON1 (fixed 1.00),
 * and Unknown -> zero.
 */
public interface DiscountPolicy {
    Money discount(Money subtotal);
    boolean printsLine(Money computedDiscount);
    String code(); // Original code identity for LAST_DISCOUNT_CODE behavior
}

/** Unknown codes map to zero discount and no printed discount line, but retain the code if you still store it globally. */
final class UnknownCodeNoDiscount implements DiscountPolicy {
    private final String unknown;
    UnknownCodeNoDiscount(String unknown) { this.unknown = unknown; }
    @Override
    public Money discount(Money subtotal) { return Money.zero(); }
    @Override
    public boolean printsLine(Money computedDiscount) { return false; }
    @Override
    public String code() { return unknown; }
}

