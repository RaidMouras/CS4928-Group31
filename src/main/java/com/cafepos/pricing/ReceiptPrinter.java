package com.cafepos.pricing;

public final class ReceiptPrinter {
    public String format(String recipe, int qty, PricingService.PricingResult pr, int taxPercent) {
        StringBuilder b = new StringBuilder();
        b.append("Order ").append(recipe).append(" x").append(qty).append("\n");
        b.append("Subtotal ").append(pr.subtotal()).append("\n");
        if (pr.discount().asBigDecimal().signum() > 0)
            b.append("Discount -").append(pr.discount()).append("\n");
        b.append("Tax ").append(taxPercent).append("% ").append(pr.tax()).append("\n");
        b.append("Total ").append(pr.total());
        return b.toString();
    }
}
