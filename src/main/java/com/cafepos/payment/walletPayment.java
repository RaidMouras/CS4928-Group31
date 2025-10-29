package com.cafepos.payment;

import com.cafepos.common.Money;

public final class walletPayment implements paymentStrategy {
    private final String walletId;

    public walletPayment(String walletId) {
        if (walletId == null || walletId.isBlank()) {
            throw new IllegalArgumentException("wallet ID required");
        }
        this.walletId = walletId;
    }
    @Override
    public void pay(Money total) {
        System.out.println("[Wallet] Customer paid " + total + " EUR via wallet " + walletId);    }
}
