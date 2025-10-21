package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.pricing.TaxPolicy;
import com.cafepos.pricing.PercentTaxPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaxPolicyTests {

    @Test
    void ten_percent_tax_on_round_amounts() {
        TaxPolicy p = new PercentTaxPolicy(10);
        Money discounted = Money.of(10.00);
        Money tax = p.tax(discounted);
        assertEquals("1.00", tax.asBigDecimal().toPlainString(), "10% of 10.00 should be 1.00");
        assertEquals("Tax (10%):", p.label(10), "Label must match receipt format exactly");
    }

    @Test
    void ten_percent_tax_with_two_decimals() {
        TaxPolicy p = new PercentTaxPolicy(10);
        Money discounted = Money.of(7.41);
        Money tax = p.tax(discounted);
        assertEquals("0.74", tax.asBigDecimal().toPlainString(), "10% of 7.41 should be 0.74 (HALF_UP to 2 dp)");
    }

    @Test
    void zero_amount_produces_zero_tax() {
        TaxPolicy p = new PercentTaxPolicy(10);
        Money discounted = Money.zero();
        Money tax = p.tax(discounted);
        assertEquals("0.00", tax.asBigDecimal().toPlainString(), "10% of 0.00 should be 0.00");
    }

    @Test
    void tiny_amount_rounds_half_up_to_two_decimals() {
        TaxPolicy p = new PercentTaxPolicy(10);
        Money discounted = Money.of(0.01); // 10% = 0.001 -> 0.00 with HALF_UP at 2 dp
        Money tax = p.tax(discounted);
        assertEquals("0.00", tax.asBigDecimal().toPlainString(), "Small fractions should round to 0.00 with HALF_UP");
    }

    @Test
    void label_reflects_configured_percent() {
        TaxPolicy p = new PercentTaxPolicy(7);
        assertEquals("Tax (7%):", p.label(7), "Label must be 'Tax (<percent>%):'");
    }

    @Test
    void different_percentages_compute_accordingly() {
        TaxPolicy p5 = new PercentTaxPolicy(5);
        TaxPolicy p20 = new PercentTaxPolicy(20);
        Money base = Money.of(12.34);
        assertEquals("0.62", p5.tax(base).asBigDecimal().toPlainString(), "5% of 12.34 = 0.617 -> 0.62");
        assertEquals("2.47", p20.tax(base).asBigDecimal().toPlainString(), "20% of 12.34 = 2.468 -> 2.47");
    }
}
