package com.cafepos.observer;

import com.cafepos.domain.order;

public final class DeliveryDesk implements OrderObserver {
    @Override
    public void updated(order order, String eventType) {
        if ("ready".equals(eventType)) {
            System.out.println("[Delivery] Order #" + order.id() + " is ready for delivery");
        }
    }}
