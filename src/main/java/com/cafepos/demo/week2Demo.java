package com.cafepos.demo;

import com.cafepos.catalog.catalog;
import com.cafepos.catalog.inMemoryCatalog;
import com.cafepos.common.money;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.orderIds;
import com.cafepos.domain.simpleProduct;

import java.math.BigDecimal;

public final class week2Demo {
    public static void main(String[] args) {
        catalog catalog = new inMemoryCatalog();
        catalog.add(new simpleProduct("P-ESP", "Espresso", money.of(BigDecimal.valueOf(2.50))));
        catalog.add(new simpleProduct("P-CCK", "Chocolate Cookie", money.of(BigDecimal.valueOf(3.50))));
        order order = new order(orderIds.next());
        order.addItem(new lineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order.addItem(new lineItem(catalog.findById("P-CCK").orElseThrow(), 1));
        int taxPct = 10;
        System.out.println("Order #" + order.id());
        System.out.println("Items: " + order.items().size());
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (" + taxPct + "%): " + order.taxAtPercent(taxPct));
        System.out.println("Total: " + order.totalWithTax(taxPct));
    }
}
