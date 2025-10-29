package com.cafepos.pricing;

import com.cafepos.common.Money;
import java.math.BigDecimal;


public interface DiscountPolicy {
    Money discount(Money subtotal);
    boolean printsLine(Money computedDiscount);
    String code(); // Original code identity for LAST_DISCOUNT_CODE behavior
}

