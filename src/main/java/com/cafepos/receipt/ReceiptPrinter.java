package com.cafepos.receipt;

import com.cafepos.common.Money;
import com.cafepos.pricing.PricingService;

public final class ReceiptPrinter {
    public String build(String recipe,
                        int qty,
                        Money subtotal,
                        boolean showDiscountLine,
                        Money discount,
                        int taxPercent,
                        Money tax,
                        Money total) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order (").append(recipe).append(")x").append(qty).append("\n");
        sb.append("Subtotal: ").append(subtotal).append("\n");
        if (showDiscountLine && discount.asBigDecimal().signum() > 0) {
            sb.append("Discount: -").append(discount).append("\n");
        }
        sb.append("Tax (").append(taxPercent).append("%):").append(tax).append("\n");
        sb.append("Total: ").append(total);
        return sb.toString();
    }

    public String buildAndMaybePrint(String recipe,
                                     int qty,
                                     Money subtotal,
                                     boolean showDiscountLine,
                                     Money discount,
                                     int taxPercent,
                                     Money tax,
                                     Money total,
                                     boolean printReceipt) {
        String out = build(recipe, qty, subtotal, showDiscountLine, discount, taxPercent, tax, total);
        if (printReceipt) System.out.println(out);
        return out;
    }

    public String format(String recipe, int qty, PricingService.PricingResult pr, int taxPercent) {
    return build(recipe,
            qty,
            pr.subtotal(),
            pr.discount().asBigDecimal().signum() > 0,
            pr.discount(),
            taxPercent,
            pr.tax(),
            pr.total());
}
}