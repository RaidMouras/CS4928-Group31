package com.cafepos.payment;

import com.cafepos.domain.Order;

public final class walletPayment implements paymentStrategy {
    private final String walletId;

    public walletPayment(String walletId) {
        if (walletId == null || walletId.isBlank()) {
            throw new IllegalArgumentException("wallet ID required");
        }
        this.walletId = walletId;
    }

    @Override
    public void pay(Order order) {
        System.out.println("[Wallet] Customer paid " + order.totalWithTax(10) + " EUR via wallet " + walletId);
    }
}