package com.cafepos.domain;
import com.cafepos.decorator.Priced;
import com.cafepos.catalog.product;
import com.cafepos.common.money;

public final class simpleProduct implements product, Priced {
    private final String id;
    private final String name;
    private final money basePrice;

    public simpleProduct(String id, String name, money basePrice){
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id required");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name required");
        if (basePrice == null)
            throw new IllegalArgumentException("basePrice required");
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public money basePrice() {
        return basePrice;
    }

    @Override
    public money price() {
        return basePrice;
    }
}
