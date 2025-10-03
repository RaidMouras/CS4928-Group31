package com.cafepos.domain;
import com.cafepos.common.money;
import com.cafepos.observer.OrderObserver;
import com.cafepos.observer.OrderPublisher;
import com.cafepos.payment.paymentStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class order implements OrderPublisher {
    private final long id;
    private final List<lineItem> items = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    public order(long id) { // setter
        if (id <= 0)
            throw new IllegalArgumentException("id must be positive");
        this.id = id;
    }

    public long id() { // getter
        return id;
    }

    @Override
    public void register(OrderObserver o) {
        if (o == null) throw new IllegalArgumentException("observer required");
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void unregister(OrderObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(order order, String eventType) {

    }

    private void notifyObservers(String eventType) {
        for (OrderObserver o : observers) {
            o.updated(this, eventType);
        }
    }

    public List<lineItem> items() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(lineItem li) {
        if (li == null)
            throw new IllegalArgumentException("line item required");
        items.add(li);
        notifyObservers("itemAdded");
    }

    public money subtotal() {
        return items.stream().map(lineItem::lineTotal).reduce(money.zero(), money::add);
    }

    public money taxAtPercent(int percent) {
        if (percent < 0)
            throw new IllegalArgumentException("Tax percentage cannot be negative");
        double taxRate = percent / 100.0;
        return subtotal().multiply(taxRate);
    }

    public money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }

    public void pay(paymentStrategy strategy) {
        if (strategy == null)
            throw new IllegalArgumentException("strategy required");
        strategy.pay(this);
        notifyObservers("paid");
    }

    public void markReady() {
        notifyObservers("ready");
    }
}
