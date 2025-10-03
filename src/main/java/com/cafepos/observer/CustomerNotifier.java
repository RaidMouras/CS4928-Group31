package com.cafepos.observer;

import com.cafepos.domain.order;

public final class CustomerNotifier implements OrderObserver {
    @Override
    public void updated(order order, String eventType) {
        System.out.println("[Customer] Dear customer, your Order #"
                + order.id() + " has been updated: " + eventType + ".");
    }
}