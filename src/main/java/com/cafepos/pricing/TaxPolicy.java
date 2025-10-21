package com.cafepos.pricing;

import com.cafepos.common.Money;

public interface TaxPolicy {
    Money tax(Money discounted);
    String label(int taxPercent); // "Tax (<percent>%):"
}
