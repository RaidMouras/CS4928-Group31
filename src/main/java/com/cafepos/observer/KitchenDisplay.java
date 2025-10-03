package com.cafepos.observer;

import com.cafepos.domain.order;

public class KitchenDisplay implements OrderObserver{
    @Override
    public void updated(order order, String eventType) {
        if ("itemAdded".equals(eventType)) {
            System.out.println("[Kitchen] Order #" + order.id() + ": item added");
        } else if ("paid".equals(eventType)) {
            System.out.println("[Kitchen] Order #" + order.id() + ": payment received");
        }
    }
}
