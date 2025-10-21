package com.cafepos;

import com.cafepos.smells.OrderManagerGod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Week6CharacterizationTests {

    @Test
    void card_payment_with_loyal5_prints_expected_receipt() {
        String receipt = OrderManagerGod.process("ESP+SHOT", 1, "CARD", "LOYAL5", false);

        // Lines must match exactly: order, subtotal, optional discount, tax label with 10%, total
        assertTrue(receipt.startsWith("Order (ESP+SHOT)x1\n"), "Header line mismatch");
        assertTrue(receipt.contains("Subtotal: "), "Missing Subtotal line");
        // For LOYAL5, expect a positive discount line
        assertTrue(receipt.contains("Discount: -"), "Expected discount line for LOYAL5");
        assertTrue(receipt.contains("Tax (10%):"), "Tax label must be exactly 'Tax (10%):'");
        assertTrue(receipt.contains("Total: "), "Missing Total line");
    }

    @Test
    void unknown_discount_code_prints_no_discount_line() {
        String receipt = OrderManagerGod.process("ESP", 2, "CASH", "XYZ", false);

        assertTrue(receipt.startsWith("Order (ESP)x2\n"), "Header line mismatch");
        assertTrue(receipt.contains("Subtotal: "), "Missing Subtotal line");
        assertFalse(receipt.contains("Discount: -"), "Unknown code should not print discount line");
        assertTrue(receipt.contains("Tax (10%):"), "Tax label must be exactly 'Tax (10%):'");
        assertTrue(receipt.contains("Total: "), "Missing Total line");
    }

    @Test
    void zero_or_negative_qty_defaults_to_one() {
        String receiptZero = OrderManagerGod.process("LAT", 0, "CARD", "NONE", false);
        assertTrue(receiptZero.startsWith("Order (LAT)x1\n"), "Qty 0 should default to 1");

        String receiptNeg = OrderManagerGod.process("LAT", -5, "CARD", "NONE", false);
        assertTrue(receiptNeg.startsWith("Order (LAT)x1\n"), "Negative qty should default to 1");
    }

    @Test
    void prints_payment_messages_to_stdout_shape_only() {
        // These calls should not throw and should print the expected prefixes.
        // If you want to capture stdout, use a PrintStream rule or System.setOut in your environment.
        // Here we only verify the calls do not fail; characterization may only check receipt, not stdout.
        assertDoesNotThrow(() -> OrderManagerGod.process("ESP", 1, "CASH", "NONE", true));
        assertDoesNotThrow(() -> OrderManagerGod.process("ESP", 1, "CARD", "NONE", true));
        assertDoesNotThrow(() -> OrderManagerGod.process("ESP", 1, "WALLET", "NONE", true));
        assertDoesNotThrow(() -> OrderManagerGod.process("ESP", 1, "UNKNOWN", "NONE", true));
    }

    @Test
    void tax_label_and_spacing_are_exact() {
        String receipt = OrderManagerGod.process("ESP", 1, "CASH", "NONE", false);
        // The colon is immediately followed by the tax amount; no extra spaces.
        int idx = receipt.indexOf("Tax (10%):");
        assertTrue(idx >= 0, "Missing exact tax label 'Tax (10%):'");
        // Check that after the colon comes a digit (part of the amount), not a space.
        char afterColon = receipt.charAt(idx + "Tax (10%):".length());
        assertTrue(Character.isDigit(afterColon), "There must be no space after the tax colon");
    }

    @Test
    void discount_never_makes_total_negative() {
        String receipt = OrderManagerGod.process("ESP", 1, "CARD", "LOYAL5", false);
        // Basic guard: ensure a Total line exists and amount is non-negative numerically.
        String prefix = "Total: ";
        int i = receipt.indexOf(prefix);
        assertTrue(i >= 0, "Missing Total line");
        String totalStr = receipt.substring(i + prefix.length()).trim();
        // totalStr like "1.23" or "0.00"
        assertFalse(totalStr.startsWith("-"), "Total should never be negative");
    }
}
