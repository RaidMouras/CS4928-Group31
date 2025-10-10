package com.cafepos.decorator;

import com.cafepos.catalog.Priced;
import com.cafepos.common.money;
import com.cafepos.domain.product;

public final class ExtraShot extends ProductDecorator {
    private static final money SURCHARGE = money.of(0.80);
    public ExtraShot(product base) { super(base); }

    @Override public String name() { return base.name() + " + Extra Shot"; }

    @Override public money price() {
        money basePrice = (base instanceof Priced p) ? p.price() : base.basePrice();
        return basePrice.add(SURCHARGE);
    }
}

