package com.cafepos.domain;
import com.cafepos.decorator.Priced;
import com.cafepos.catalog.product;
import com.cafepos.common.money;

public final class lineItem {
    private final product product;
    private final int quantity;

    public lineItem(product product, int quantity) {
        if (product == null)
            throw new IllegalArgumentException("product required");
        if (quantity <= 0)
            throw new IllegalArgumentException("quantity must be > 0");
        this.product = product;
        this.quantity = quantity;
    }


    public product product() {

        return product;
    }

    public int quantity() {

        return quantity;
    }

    public money lineTotal() {
        money unit = (product instanceof Priced p) ? p.price() : product.basePrice();
        return unit.multiply(quantity);
    }}
