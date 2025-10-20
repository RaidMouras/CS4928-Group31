package com.cafepos;

import com.cafepos.catalog.product;
import com.cafepos.common.money;
import com.cafepos.domain.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class orderTest {

    @Test
    public void noTaxTotal() {

        product Espresso = new simpleProduct("P-ESP", "Espresso", money.of(2.50));
        order Order = new order(orderIds.next());
        Order.addItem(new lineItem(Espresso, 5));

        assertEquals(money.of(12.50), Order.subtotal());
    }

    @Test
    public void TaxCalc() {

        product Espresso = new simpleProduct("P-ESP", "Espresso", money.of(2.50));
        order Order = new order(orderIds.next());
        Order.addItem(new lineItem(Espresso, 5));

        assertEquals(money.of(1.25), Order.taxAtPercent(10));
    }

    @Test
    public void TotalTest() {

        product Espresso = new simpleProduct("P-ESP", "Espresso", money.of(2.50));
        order Order = new order(orderIds.next());
        Order.addItem(new lineItem(Espresso, 5));

        assertEquals(money.of(13.75), Order.totalWithTax(10));
    }
}
