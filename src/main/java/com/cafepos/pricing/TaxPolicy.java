package com.cafepos.pricing;

import com.cafepos.common.Money;

import java.math.BigDecimal;

public interface TaxPolicy {
    Money tax(Money discounted);
    String label(int taxPercent); // must produce "Tax (<percent>%):"
}