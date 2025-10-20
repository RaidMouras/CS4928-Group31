package com.cafepos.demo;

import com.cafepos.catalog.catalog;
import com.cafepos.catalog.inMemoryCatalog;
import com.cafepos.common.money;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.orderIds;
import com.cafepos.domain.simpleProduct;
import com.cafepos.payment.cardPayment;
import com.cafepos.payment.cashPayment;
import com.cafepos.payment.walletPayment;

import java.math.BigDecimal;

public final class week3Demo {
    public static void main(String[] args) {
        catalog catalog = new inMemoryCatalog();
        catalog.add(new simpleProduct("P-ESP", "Espresso", money.of(BigDecimal.valueOf(2.50))));
        catalog.add(new simpleProduct("P-CCK", "Chocolate Cookie", money.of(BigDecimal.valueOf(3.50))));

// Cash payment
        order order1 = new order(orderIds.next());
        order1.addItem(new lineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order1.addItem(new lineItem(catalog.findById("P-CCK").orElseThrow(), 1));
            System.out.println("Order #" + order1.id() + " Total: " + order1.totalWithTax(10));
        order1.pay(new cashPayment());

// Card payment
        order order2 = new order(orderIds.next());
        order2.addItem(new lineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order2.addItem(new lineItem(catalog.findById("P-CCK").orElseThrow(), 1));
            System.out.println("Order #" + order2.id() + " Total: " + order2.totalWithTax(10));
        order2.pay(new cardPayment("1234567812342345"));
    }
}
