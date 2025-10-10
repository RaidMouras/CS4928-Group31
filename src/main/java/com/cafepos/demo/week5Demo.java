package com.cafepos.demo;

import com.cafepos.Factory.ProductFactory;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.orderIds;
import com.cafepos.domain.product;

public final class week5Demo {
    public static void main(String[] args) {
        ProductFactory factory = new ProductFactory();
        product p1 = factory.create("ESP+SHOT+OAT"); // Espresso + Extra Shot + Oat
        product p2 = factory.create("LAT+L"); // Large Latte
        order order = new order(orderIds.next());
        order.addItem(new lineItem(p1, 1));
        order.addItem(new lineItem(p2, 2));
        System.out.println("Order #" + order.id());
        for (lineItem li : order.items()) {
            System.out.println(" - " + li.product().name() + " x" + li.quantity() + " = " + li.lineTotal());
        }
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (10%): " + order.taxAtPercent(10));
        System.out.println("Total: " + order.totalWithTax(10));
    }
}
