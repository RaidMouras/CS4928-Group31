package com.cafepos;

import com.cafepos.smells.OrderManagerGod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Week6CharacterizationTests {

    @BeforeEach
    void resetGlobals() {
        OrderManagerGod.TAX_PERCENT = 10;
        OrderManagerGod.LAST_DISCOUNT_CODE = null;
    }

    @Test
    void no_discount_cash_payment_receipt_shape_and_globals() {
        String r = OrderManagerGod.process("ESP", 1, "CASH", "NONE", false);

        // Format/structure
        assertTrue(r.startsWith("Order ("), "Receipt must start with 'Order ('");
        assertTrue(r.contains(")x1\n"), "Order line must render ')x1' followed by newline");
        assertTrue(r.contains("Subtotal: "), "Must include 'Subtotal: '");
        assertFalse(r.contains("Discount:"), "No discount line when discount is zero (NONE)");
        assertTrue(r.contains("Tax (10%):"), "Tax label must be exactly 'Tax (10%):'");
        assertTrue(r.indexOf("Subtotal: ") < r.indexOf("Tax (10%):"), "Subtotal precedes Tax");
        assertTrue(r.indexOf("Tax (10%):") < r.indexOf("Total: "), "Tax precedes Total");
        assertTrue(r.endsWith("Total: " + r.substring(r.indexOf("Total: ") + 7)), "Total must be last line");

        // Global side effect
        assertEquals("NONE", OrderManagerGod.LAST_DISCOUNT_CODE, "LAST_DISCOUNT_CODE should capture provided code");
    }

    @Test
    void loyalty5_two_latte_card_includes_discount_line_and_ordering() {
        String r = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);

        assertTrue(r.contains("Subtotal: "), "Must include Subtotal");
        assertTrue(r.contains("Discount: -"), "Percent discount must print as 'Discount: -<amount>'");
        assertTrue(r.contains("Tax (10%):"), "Must include exact tax label");
        assertTrue(r.contains("Total: "), "Must include Total line");

        int iOrder = r.indexOf("Order (");
        int iSubtotal = r.indexOf("Subtotal: ");
        int iDiscount = r.indexOf("Discount: -");
        int iTax = r.indexOf("Tax (10%):");
        int iTotal = r.indexOf("Total: ");

        assertTrue(iOrder >= 0 && iOrder < iSubtotal, "Order precedes Subtotal");
        assertTrue(iSubtotal < iDiscount, "Subtotal precedes Discount");
        assertTrue(iDiscount < iTax, "Discount precedes Tax");
        assertTrue(iTax < iTotal, "Tax precedes Total");

        assertEquals("LOYAL5", OrderManagerGod.LAST_DISCOUNT_CODE, "Global should capture applied code");
    }

    @Test
    void coupon1_wallet_qty_zero_clamps_to_one_and_prints_discount_line() {
        String r = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", "COUPON1", false);

        assertTrue(r.contains(")x1\n"), "qty <= 0 must clamp to 1 and show ')x1'");
        assertTrue(r.contains("Discount: -"), "Fixed coupon must print discount line");
        assertTrue(r.contains("Tax (10%):"), "Must include exact tax label");
        assertTrue(r.endsWith("Total: " + r.substring(r.indexOf("Total: ") + 7)), "Total must be last line");

        assertEquals("COUPON1", OrderManagerGod.LAST_DISCOUNT_CODE, "Global should store code even for fixed coupons");
    }

    @Test
    void unknown_discount_code_prints_no_discount_and_sets_global() {
        String r = OrderManagerGod.process("CAP+OAT", 1, "CARD", "XYZ", false);

        assertFalse(r.contains("Discount:"), "Unknown code results in zero discount and no discount line");
        assertTrue(r.contains("Subtotal: "), "Subtotal must print");
        assertTrue(r.contains("Tax (10%):"), "Tax must print");
        assertTrue(r.contains("Total: "), "Total must print");

        assertEquals("XYZ", OrderManagerGod.LAST_DISCOUNT_CODE, "LAST_DISCOUNT_CODE should capture unknown code too");
    }

    @Test
    void null_discount_code_prints_no_discount_and_does_not_touch_global() {
        String r = OrderManagerGod.process("LAT", 1, "CASH", null, false);

        assertFalse(r.contains("Discount:"), "No discount line when discountCode is null");
        assertNull(OrderManagerGod.LAST_DISCOUNT_CODE, "Global must remain null when code is null");
    }

    @Test
    void payment_variants_do_not_change_receipt_shape() {
        String cash = OrderManagerGod.process("ESP", 1, "CASH", "NONE", false);
        String card = OrderManagerGod.process("ESP", 1, "CARD", "NONE", false);
        String wallet = OrderManagerGod.process("ESP", 1, "WALLET", "NONE", false);
        String unknown = OrderManagerGod.process("ESP", 1, "FOO", "NONE", false);

        for (String r : new String[]{cash, card, wallet, unknown}) {
            assertTrue(r.startsWith("Order ("), "Receipt must start with 'Order ('");
            assertTrue(r.contains("Subtotal: "), "Must include Subtotal line");
            assertTrue(r.contains("Tax (10%):"), "Must include exact Tax label");
            assertTrue(r.contains("\nTotal: "), "Must include Total line");
        }
    }
}
