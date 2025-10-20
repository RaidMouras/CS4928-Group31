package com.cafepos.Factory;

import com.cafepos.common.money;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.SizeLarge;
import com.cafepos.decorator.Syrup;
import com.cafepos.catalog.product;
import com.cafepos.domain.simpleProduct;

import java.math.BigDecimal;

public final class ProductFactory {
    public product create(String recipe) {
        if (recipe == null || recipe.isBlank())
            throw new IllegalArgumentException("recipe required");

        String[] parts = java.util.Arrays.stream(recipe.split("\\+"))
                .map(String::trim)
                .map(String::toUpperCase)
                .toArray(String[]::new);

        product p = switch (parts[0]) {
            case "ESP" -> new simpleProduct("P-ESP", "Espresso", money.of(BigDecimal.valueOf(2.50)));
            case "LAT" -> new simpleProduct("P-LAT", "Latte", money.of(BigDecimal.valueOf(3.20)));
            case "CAP" -> new simpleProduct("P-CAP", "Cappuccino", money.of(BigDecimal.valueOf(3.00)));
            default -> throw new IllegalArgumentException("Unknown base: " + parts[0]);
        };

        for (int i = 1; i < parts.length; i++) {
            p = switch (parts[i]) {
                case "SHOT" -> new ExtraShot(p);
                case "OAT" -> new OatMilk(p);
                case "SYP" -> new Syrup(p);
                case "L" -> new SizeLarge(p);
                default -> throw new IllegalArgumentException("Unknown addon: " + parts[i]);
            };
        }
        return p;
    }
}
