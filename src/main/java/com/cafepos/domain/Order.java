package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.observer.OrderObserver;
import com.cafepos.payment.paymentStrategy;

import java.math.BigDecimal;
import java.util.*;

public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(long id) { this.id = id; }
    public long id() { return id; }
    public List<LineItem> items() { return List.copyOf(items); }
    public void addItem(LineItem li) { items.add(li); }
    public Money subtotal() { return items.stream().map(LineItem::lineTotal).reduce(Money.zero(), Money::add); }
    public Money taxAtPercent(int percent) {
        BigDecimal tax = subtotal().asBigDecimal().multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100));
        return Money.of(tax);
    }
    public Money totalWithTax(int percent) { return subtotal().add(taxAtPercent(percent)); }

    public void pay(paymentStrategy strategy) {
        if (strategy == null)
            throw new IllegalArgumentException("strategy required");
        strategy.pay(this.subtotal());
        notifyObservers("paid");
    }

    public void markReady() {
        notifyObservers("ready");
    }
    public void notifyObservers(String eventType) {
        for (OrderObserver o : observers) {
            o.updated(this, eventType);
        }
    }
}
