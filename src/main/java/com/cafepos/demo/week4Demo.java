package com.cafepos.demo;

import com.cafepos.catalog.catalog;
import com.cafepos.catalog.inMemoryCatalog;
import com.cafepos.common.money;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.orderIds;
import com.cafepos.domain.simpleProduct;
import com.cafepos.observer.CustomerNotifier;
import com.cafepos.observer.DeliveryDesk;
import com.cafepos.observer.KitchenDisplay;
import com.cafepos.payment.cashPayment;

import java.math.BigDecimal;

public final class week4Demo {
    public static void main(String[] args) {
        catalog catalog = new inMemoryCatalog();
        catalog.add(new simpleProduct("P-ESP", "Espresso", money.of(BigDecimal.valueOf(2.50))));
        order order = new order(orderIds.next());
        order.register(new KitchenDisplay());
        order.register(new DeliveryDesk());
        order.register(new CustomerNotifier());
        order.addItem(new lineItem(catalog.findById("P-ESP").orElseThrow(), 1));
        order.pay(new cashPayment());
        order.markReady();
    }
}
