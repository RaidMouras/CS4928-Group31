package com.cafepos.payment;

import com.cafepos.common.Money;

public final class walletPayment implements paymentStrategy {
    @Override
    public void pay(Money total) {
        System.out.println("[Wallet] Customer paid " + total + "EUR via wallet user-wallet-789");
    }
}
