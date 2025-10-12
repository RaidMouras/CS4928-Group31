package com.cafepos;

import com.cafepos.common.money;
import com.cafepos.catalog.Priced;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.simpleProduct;
import com.cafepos.domain.product;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.Syrup;
import com.cafepos.decorator.SizeLarge;
import com.cafepos.Factory.ProductFactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductDecoratorAndFactoryTest {

    @Test
    void single_decorator_adds_correct_surcharge_and_label() {
        product espresso = new simpleProduct("P-ESP", "Espresso", money.of(2.50));
        product withShot = new ExtraShot(espresso);

        assertEquals("Espresso + Extra Shot", withShot.name());
        assertEquals(money.of(3.30), ((Priced) withShot).price());
    }

    @Test
    void multiple_decorators_stack_correctly() {
        product espresso = new simpleProduct("P-ESP", "Espresso", money.of(2.50));
        product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));

        assertEquals("Espresso + Extra Shot + Oat Milk (Large)", decorated.name());
        assertEquals(money.of(4.50), ((Priced) decorated).price());
    }

    @Test
    void decoration_order_does_not_affect_total_price() {
        product espresso = new simpleProduct("P-ESP", "Espresso", money.of(2.50));

        product one = new SizeLarge(new OatMilk(new ExtraShot(espresso)));
        product two = new OatMilk(new SizeLarge(new ExtraShot(espresso)));

        assertEquals(((Priced) one).price(), ((Priced) two).price(),
                "Total surcharge should be identical regardless of wrapping order");
    }

    @Test
    void factory_builds_correct_decorated_product() {
        ProductFactory factory = new ProductFactory();
        product p = factory.create("ESP+SHOT+OAT");

        assertTrue(p.name().contains("Espresso"));
        assertTrue(p.name().contains("Oat Milk"));
        assertEquals(money.of(3.80), ((Priced) p).price());
    }

    @Test
    void order_uses_decorated_price_in_totals() {
        product espresso = new simpleProduct("P-ESP", "Espresso", money.of(2.50));
        product withShot = new ExtraShot(espresso); // should cost 3.30

        order o = new order(1);
        o.addItem(new lineItem(withShot, 2)); // 3.30 × 2

        assertEquals(money.of(6.60), o.subtotal());
    }

    @Test
    void factory_and_manual_chaining_build_equivalent_drinks() {
        ProductFactory factory = new ProductFactory();
        product viaFactory = factory.create("ESP+SHOT+OAT+L");

        product viaManual = new SizeLarge(
                new OatMilk(
                        new ExtraShot(
                                new simpleProduct("P-ESP", "Espresso", money.of(2.50)))
                )
        );

        assertEquals(viaManual.name(), viaFactory.name());
        assertEquals(((Priced) viaManual).price(), ((Priced) viaFactory).price());

        order orderA = new order(10);
        orderA.addItem(new lineItem(viaFactory, 1));

        order orderB = new order(11);
        orderB.addItem(new lineItem(viaManual, 1));

        assertEquals(orderA.subtotal(), orderB.subtotal());
        assertEquals(orderA.totalWithTax(10), orderB.totalWithTax(10));
    }
}
