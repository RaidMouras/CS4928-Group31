package com.cafepos.observer;

import com.cafepos.domain.order;

public interface OrderPublisher {
    void register(OrderObserver o);
    void unregister(OrderObserver o);
    void notifyObservers(order order, String eventType);
}
