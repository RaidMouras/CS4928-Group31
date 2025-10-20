package com.cafepos.pricing;

import com.cafepos.common.money;

public interface TaxPolicy {
    money taxOn(money amount);
}
