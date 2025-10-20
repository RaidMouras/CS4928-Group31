package com.cafepos.checkout;

import com.cafepos.common.money;
import com.cafepos.Factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.domain.product;
import com.cafepos.decorator.Priced;

public final class CheckoutService {
    private final ProductFactory factory;
    private final PricingService pricing;
    private final ReceiptPrinter printer;
    private final int taxPercent;

    public CheckoutService(ProductFactory factory, PricingService pricing, ReceiptPrinter printer, int taxPercent) {
        this.factory = factory;
        this.pricing = pricing;
        this.printer = printer;
        this.taxPercent = taxPercent;
    }

    public String checkout(String recipe, int qty) {
        product p = factory.create(recipe);
        if (qty <= 0) qty = 1;

        money unit;
        if (p instanceof Priced priced)
            unit = priced.price();
        else
            unit = p.basePrice();

        money subtotal = unit.multiply(qty);
        var result = pricing.price(subtotal);
        return printer.format(recipe, qty, result, taxPercent);
    }
}
