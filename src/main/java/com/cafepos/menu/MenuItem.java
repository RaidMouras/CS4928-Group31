package com.cafepos.menu;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import java.util.Collections;
import java.util.Iterator;


public final class MenuItem extends MenuComponent {

    private final Product product;
    private final boolean vegetarian;

    public MenuItem(Product product, boolean vegetarian) {
        if (product == null) throw new IllegalArgumentException("product required");
        this.product = product;
        this.vegetarian = vegetarian;
    }

    @Override public String name() { return product.name(); }
    @Override public Money price() { return product.basePrice(); }
    @Override public boolean vegetarian() { return vegetarian; }

    @Override public Iterator<MenuComponent> iterator() {
        return Collections.emptyIterator();
    }

    @Override public void print() {
        String veg = vegetarian ? " (V)" : "";
        System.out.println(" - " + product.name() + veg + " = " + product.basePrice());
    }
}
