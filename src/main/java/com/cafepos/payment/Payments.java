package com.cafepos.payment;

public final class Payments {
    private Payments() {}

    public static paymentStrategy of(String type) {
        if (type == null) return new unknownPayment();
        if (type.equalsIgnoreCase("CASH")) return new cashPayment();
        if (type.equalsIgnoreCase("CARD")) return new cardPayment("12345678");
        if (type.equalsIgnoreCase("WALLET")) return new walletPayment("12345678");
        return new unknownPayment();
    }
}
