package com.cafepos.pricing;

/** Factory for selecting a policy by discountCode, matching legacy semantics. */
public final class DiscountPolicies {
    private DiscountPolicies() {}

    public static DiscountPolicy fromCode(String discountCode) {
        if (discountCode == null) return new NoDiscount();         // legacy: skip setting LAST_DISCOUNT_CODE in caller
        if ("LOYAL5".equalsIgnoreCase(discountCode)) return new Loyalty5Percent();
        if ("COUPON1".equalsIgnoreCase(discountCode)) return new CouponFixed1();
        if ("NONE".equalsIgnoreCase(discountCode)) return new NoDiscount();
        return new UnknownCodeNoDiscount(discountCode);            // legacy: zero discount, optionally still record the code
    }
}
