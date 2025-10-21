package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.pricing.DiscountPolicy;
import com.cafepos.pricing.DiscountPolicies;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountPolicyTests {

    @Test
    void no_discount_policy_produces_zero_and_printsline_false() {
        DiscountPolicy p = DiscountPolicies.fromCode("NONE");
        Money subtotal = Money.of(10.00);
        Money d = p.discount(subtotal);
        assertEquals("0.00", d.asBigDecimal().toPlainString(), "NONE must yield zero discount");
        assertFalse(p.printsLine(d), "No discount line for zero discount");
        assertEquals("NONE", p.code(), "Identity should reflect NONE");
    }

    @Test
    void null_code_maps_to_no_discount_and_does_not_print_line() {
        DiscountPolicy p = DiscountPolicies.fromCode(null);
        Money subtotal = Money.of(25.00);
        Money d = p.discount(subtotal);
        assertEquals("0.00", d.asBigDecimal().toPlainString(), "null code -> zero discount");
        assertFalse(p.printsLine(d), "No discount line for zero discount");
        assertEquals("NONE", p.code(), "Factory returns NoDiscount which identifies as NONE");
    }

    @Test
    void loyalty5_percent_of_subtotal_rounded_half_up() {
        DiscountPolicy p = DiscountPolicies.fromCode("LOYAL5");
        Money subtotal = Money.of(7.80); // e.g., 2 large lattes at 3.90 each
        Money d = p.discount(subtotal);
        assertEquals("0.39", d.asBigDecimal().toPlainString(), "5% of 7.80 = 0.39");
        assertTrue(p.printsLine(d), "Positive discount prints line");
        assertEquals("LOYAL5", p.code(), "Identity should reflect LOYAL5");
    }

    @Test
    void coupon_fixed_1_euro_discount() {
        DiscountPolicy p = DiscountPolicies.fromCode("COUPON1");
        Money subtotal = Money.of(3.30); // e.g., one espresso with something
        Money d = p.discount(subtotal);
        assertEquals("1.00", d.asBigDecimal().toPlainString(), "Fixed coupon is exactly 1.00");
        assertTrue(p.printsLine(d), "Positive discount prints line");
        assertEquals("COUPON1", p.code(), "Identity should reflect COUPON1");
    }

    @Test
    void unknown_code_produces_zero_and_still_identifies_with_original_code() {
        DiscountPolicy p = DiscountPolicies.fromCode("XYZ");
        Money subtotal = Money.of(12.34);
        Money d = p.discount(subtotal);
        assertEquals("0.00", d.asBigDecimal().toPlainString(), "Unknown -> zero discount");
        assertFalse(p.printsLine(d), "No discount line for zero discount");
        assertEquals("XYZ", p.code(), "Unknown policy keeps original code for global tracking");
    }

    @Test
    void percent_policy_rounding_is_half_up_on_fractional_cents() {
        DiscountPolicy p = DiscountPolicies.fromCode("LOYAL5");
        Money subtotal = Money.of(0.01); // 5% of 0.01 = 0.0005 -> 0.00 with HALF_UP at 2 dp
        Money d = p.discount(subtotal);
        assertEquals("0.00", d.asBigDecimal().toPlainString(), "Scaled to 2 decimals HALF_UP");
        assertFalse(p.printsLine(d), "Zero discount should not print line");
    }
}
