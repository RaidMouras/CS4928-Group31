package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;
import com.cafepos.pricing.DiscountPolicies;
import com.cafepos.pricing.DiscountPolicy;
import com.cafepos.pricing.PercentTaxPolicy;
import com.cafepos.pricing.TaxPolicy;
import com.cafepos.receipt.ReceiptPrinter;

public class OrderManagerGod {

    public static int TAX_PERCENT = 10; // Global/Static State: TAX_PERCENT is a mutable global primitive — risky and hard to test.
    public static String LAST_DISCOUNT_CODE = null; // Global/Static State: LAST_DISCOUNT_CODE is global — risky and hard to test.

    public static String process(String recipe, int qty, String
            paymentType, String discountCode, boolean printReceipt) {
        // God Class & Long Method: One method performs creation, pricing, discounting, tax, payment I/O, and printing.
        ProductFactory factory = new ProductFactory(); // Feature Envy / Shotgun Surgery: Creation concerns embedded here; changes require editing this method.
        Product product = factory.create(recipe);      // Feature Envy / Shotgun Surgery: Product construction rules live here; edits ripple here.

        Money unitPrice;
        try {
            var priced = product instanceof com.cafepos.decorator.Priced
                    p ? p.price() : product.basePrice(); // Duplicated Logic: Pricing selection and branching done inline with domain details.
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice(); // Duplicated Logic: Repeated fallback pricing logic inline.
        }

        if (qty <= 0) qty = 1; // Primitive Obsession: business rule on primitive qty; no Quantity type.

        Money subtotal = unitPrice.multiply(qty); // Duplicated Logic: Money math performed inline.

        DiscountPolicy dPolicy = DiscountPolicies.fromCode(discountCode);
        if (discountCode != null) { // preserve legacy side effect behavior
            LAST_DISCOUNT_CODE = dPolicy.code();
        }
        Money discount = dPolicy.discount(subtotal);

        Money discounted =
                Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal())); // Duplicated Logic: BigDecimal math inline.
        if (discounted.asBigDecimal().signum() < 0) discounted =
                Money.zero(); // Duplicated Logic: rule and guard logic inline.

        TaxPolicy tPolicy = new PercentTaxPolicy(TAX_PERCENT);
        Money tax = tPolicy.tax(discounted);
        Money total = discounted.add(tax);

        if (paymentType != null) {
            if (paymentType.equalsIgnoreCase("CASH")) {
                System.out.println("[Cash] Customer paid " + total + "EUR"); // God Class & Long Method: payment I/O inside same method.
            } else if (paymentType.equalsIgnoreCase("CARD")) {
                System.out.println("[Card] Customer paid " + total + "EUR with card ****1234"); // God Class & Long Method: payment I/O.
            } else if (paymentType.equalsIgnoreCase("WALLET")) {
                System.out.println("[Wallet] Customer paid " + total + "EUR via wallet user-wallet-789"); // God Class & Long Method: payment I/O.
            } else {
                System.out.println("[UnknownPayment] " + total); // God Class & Long Method: payment I/O.
            }
        }

        ReceiptPrinter printer = new ReceiptPrinter();
        boolean showDiscountLine = dPolicy.printsLine(discount);
        return printer.buildAndMaybePrint(
                recipe,
                qty,
                subtotal,
                showDiscountLine,
                discount,
                TAX_PERCENT, // keep exact label percent
                tax,
                total,
                printReceipt
        );
    }
}
