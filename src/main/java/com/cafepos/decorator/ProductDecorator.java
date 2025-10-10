package com.cafepos.decorator;


import com.cafepos.catalog.Priced;
import com.cafepos.common.money;
import com.cafepos.domain.product;

public abstract class ProductDecorator implements product, Priced {
    protected final product base;

    protected ProductDecorator(product base) {
        if (base == null) throw new IllegalArgumentException("base product required");
        this.base = base;
    }

    @Override public String id() { return base.id(); }
    @Override public money basePrice() { return base.basePrice(); }
}
