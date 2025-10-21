package com.cafepos;


import com.cafepos.common.Money;
import com.cafepos.pricing.PricingService;
import com.cafepos.receipt.ReceiptPrinter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReceiptPrinterTests {

    @Test
    void formats_receipt_with_discount_and_tax_correctly() {
        var printer = new ReceiptPrinter();
        var result = new PricingService.PricingResult(
                Money.of(7.80),  // subtotal
                Money.of(0.39),  // discount
                Money.of(0.74),  // tax
                Money.of(8.15)   // total
        );

        String receipt = printer.format("LAT+L", 2, result, 10);

        assertTrue(receipt.startsWith("Order (LAT+L)x2"));
        assertTrue(receipt.contains("Subtotal: 7.80"));
        assertTrue(receipt.contains("Discount: -0.39"));
        assertTrue(receipt.contains("Tax (10%):0.74"));
        assertTrue(receipt.contains("Total: 8.15"));
    }

    @Test
    void omits_discount_line_when_zero() {
        var printer = new ReceiptPrinter();
        var result = new PricingService.PricingResult(
                Money.of(3.30),  // subtotal
                Money.zero(),    // no discount
                Money.of(0.33),  // tax
                Money.of(3.63)
        );

        String receipt = printer.format("ESP+SHOT", 1, result, 10);

        assertTrue(receipt.startsWith("Order (ESP+SHOT)x1"));
        assertTrue(receipt.contains("Subtotal: 3.30"));
        assertFalse(receipt.contains("Discount:"));
        assertTrue(receipt.contains("Tax (10%):0.33"));
        assertTrue(receipt.contains("Total: 3.63"));
    }
}
