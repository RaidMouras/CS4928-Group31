package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.pricing.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PricingServiceTests {

    @Test
    void loyalty_discount_then_tax_pipeline() {
        var pricing = new PricingService(
            DiscountPolicies.fromCode("LOYAL5"),
            new PercentTaxPolicy(10)
        );
        var result = pricing.price(Money.of(7.80));

        assertEquals(Money.of(0.39), result.discount());
        assertEquals(Money.of(0.74), result.tax());
        assertEquals(Money.of(8.15), result.total());
    }

    @Test
    void fixed_coupon_pipeline() {
        var pricing = new PricingService(
            DiscountPolicies.fromCode("COUPON1"),
            new PercentTaxPolicy(10)
        );
        var result = pricing.price(Money.of(3.30));

        assertEquals(Money.of(1.00), result.discount());
        assertEquals(Money.of(0.23), result.tax());
        assertEquals(Money.of(2.53), result.total());
    }
}
