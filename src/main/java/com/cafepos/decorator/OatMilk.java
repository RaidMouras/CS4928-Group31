package com.cafepos.decorator;

import com.cafepos.common.money;
import com.cafepos.catalog.product;

import java.math.BigDecimal;

public final class OatMilk extends ProductDecorator {
    private static final money SURCHARGE = money.of(BigDecimal.valueOf(0.50));
    public OatMilk(product base) { super(base); }
    @Override public String name() { return base.name() + " + Oat Milk"; }
    @Override public money price() {
        money basePrice = (base instanceof Priced p) ? p.price() : base.basePrice();
        return basePrice.add(SURCHARGE);
    }
}
