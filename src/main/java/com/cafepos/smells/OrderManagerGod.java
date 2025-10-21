package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;
import com.cafepos.pricing.DiscountPolicies;
import com.cafepos.pricing.DiscountPolicy;

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

        DiscountPolicy policy = DiscountPolicies.fromCode(discountCode);
        if (discountCode != null) { // preserve legacy side effect behavior
            LAST_DISCOUNT_CODE = policy.code();
        }
        Money discount = policy.discount(subtotal);

        Money discounted =
                Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal())); // Duplicated Logic: BigDecimal math inline.
        if (discounted.asBigDecimal().signum() < 0) discounted =
                Money.zero(); // Duplicated Logic: rule and guard logic inline.

        var tax = Money.of(discounted.asBigDecimal()
                .multiply(java.math.BigDecimal.valueOf(TAX_PERCENT)) // Primitive Obsession: TAX_PERCENT primitive used for rate.
                .divide(java.math.BigDecimal.valueOf(100)));         // Duplicated Logic: percent computation scattered inline.
        // Feature Envy / Shotgun Surgery: Tax rule embedded here; changing tax requires editing this method.

        var total = discounted.add(tax); // Duplicated Logic: total calculation inline.

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

        StringBuilder receipt = new StringBuilder();
        // God Class & Long Method: also handles printing/presentation responsibilities.
        receipt.append("Order (").append(recipe).append(")x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n"); // Duplicated Logic: formatting logic inline.
        }
        receipt.append("Tax (").append(TAX_PERCENT).append("%):").append(tax).append("\n"); // Primitive Obsession: percent label from primitive.
        receipt.append("Total: ").append(total);

        String out = receipt.toString();
        if (printReceipt) {
            System.out.println(out); // God Class & Long Method: printing I/O in same method.
        }
        return out;
    }
}
