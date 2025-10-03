package com.cafepos.observer;

import com.cafepos.domain.order;

public interface OrderObserver {
    void updated(order order, String eventType);

}
