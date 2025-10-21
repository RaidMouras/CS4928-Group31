package com.cafepos.payment;

public final class Payments {
    private Payments() {}

    public static paymentStrategy of(String type) {
        if (type == null) return new unknownPayment();
        if (type.equalsIgnoreCase("CASH")) return new cashPayment();
        if (type.equalsIgnoreCase("CARD")) return new cardPayment();
        if (type.equalsIgnoreCase("WALLET")) return new walletPayment();
        return new unknownPayment();
    }
}
