package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.catalog.Product;
import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.TaxPolicy;
import com.cafepos.receipt.ReceiptPrinter;
import com.cafepos.payment.paymentStrategy;
import com.cafepos.payment.Payments;

public class OrderManagerGod {

    private final ProductFactory factory;
    private final PricingService pricing;   // composes DiscountPolicy + TaxPolicy
    private final ReceiptPrinter printer;
    private final paymentStrategy paymentOrNull; // optional injected payment
    private final boolean useInjectedPayment;
    private final TaxPolicy taxPolicy;      // keep a reference for labeling

    public OrderManagerGod(ProductFactory factory,
                           com.cafepos.pricing.DiscountPolicy discountPolicy,
                           TaxPolicy taxPolicy,
                           ReceiptPrinter printer,
                           paymentStrategy paymentStrategy) {
        this.factory = factory;
        this.pricing = new PricingService(discountPolicy, taxPolicy);
        this.printer = printer;
        this.paymentOrNull = paymentStrategy;
        this.useInjectedPayment = (paymentStrategy != null);
        this.taxPolicy = taxPolicy;
    }

    // If tests still call static OrderManagerGod.process(...), keep a static wrapper temporarily:
    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt) {
        // Transitional compatibility: choose policies here without globals.
        var factory = new com.cafepos.factory.ProductFactory();
        var discountPolicy = com.cafepos.pricing.DiscountPolicies.fromCode(discountCode);
        var taxPolicy = new com.cafepos.pricing.PercentTaxPolicy(10); // configure percent here, not globally
        var printer = new com.cafepos.receipt.ReceiptPrinter();
        var omg = new OrderManagerGod(factory, discountPolicy, taxPolicy, printer, null);
        return omg.processInstance(recipe, qty, paymentType, discountCode, printReceipt);
    }

    // Instance version used by DI code; no globals referenced.
    public String processInstance(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt) {
        Product product = factory.create(recipe);

        Money unitPrice;
        try {
            var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }

        if (qty <= 0) qty = 1;

        Money subtotal = unitPrice.multiply(qty);

        // No LAST_DISCOUNT_CODE mutation here anymore.

        var pr = pricing.price(subtotal);

        paymentStrategy pay = useInjectedPayment ? paymentOrNull : Payments.of(paymentType);
        pay.pay(pr.total());

        boolean showDiscountLine = pr.discount().asBigDecimal().signum() > 0;

        // Derive the percent for the label from the injected TaxPolicy
        int percentForLabel = (taxPolicy instanceof com.cafepos.pricing.PercentTaxPolicy p) ? p.percent() : 10;

        return printer.buildAndMaybePrint(
                recipe,
                qty,
                pr.subtotal(),
                showDiscountLine,
                pr.discount(),
                percentForLabel,     // no TAX_PERCENT global
                pr.tax(),
                pr.total(),
                printReceipt
        );
    }
}