package com.cafepos;

import com.cafepos.domain.order;
import com.cafepos.observer.OrderObserver;

import java.util.ArrayList;
import java.util.List;

public class fakeObserver implements OrderObserver {
    private final List<String> receivedEvents = new ArrayList<>();

    @Override
    public void updated(order order, String eventType) {
        receivedEvents.add(eventType);
    }

    public List<String> getReceivedEvents() {
        return receivedEvents;
    }

    public boolean hasEvent(String event) {
        return receivedEvents.contains(event);
    }
}
