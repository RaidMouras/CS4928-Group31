package com.cafepos;

import com.cafepos.smells.OrderManagerGod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Week6CharacterizationTests {
    @Test
    void loyaltyDiscountCardPayment() {
        String receipt = OrderManagerGod.process("LATL", 2, "CARD", "LOYAL5", false);
        assertTrue(receipt.contains("Subtotal 7.80"));
        assertTrue(receipt.contains("Discount -0.39"));
        assertTrue(receipt.contains("Tax 10% 0.74"));
        assertTrue(receipt.contains("Total 8.15"));
    }
}
