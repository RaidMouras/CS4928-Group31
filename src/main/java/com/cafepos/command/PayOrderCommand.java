package com.cafepos.command;

import com.cafepos.payment.paymentStrategy;

public final class PayOrderCommand implements Command {
    private final OrderService service;
    private final paymentStrategy strategy;
    private final int taxPercent;

    public PayOrderCommand(OrderService service, paymentStrategy strategy, int taxPercent) {
        this.service = service;
        this.strategy = strategy;
        this.taxPercent = taxPercent;
    }

    @Override
    public void execute() {
        service.pay(strategy, taxPercent);
    }
}
