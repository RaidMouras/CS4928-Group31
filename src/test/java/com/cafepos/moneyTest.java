package com.cafepos;

import com.cafepos.common.money;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class moneyTest {

    @Test
    public void addMoneyTest() {
        money m1 = money.of(2.00);
        money m2 = money.of(3.00);

        assertEquals(money.of(5.00), m1.add(m2));
    }

    @Test
    public void multiplyTest() {
        money m1 = money.of(2.00);
        money m2 = money.of(3.00);

        assertEquals(money.of(6.00), m2.multiply(2));
    }

    @Test
    public void multiplyDoubleTest() {
        money m1 = money.of(2.00);
        money m2 = money.of(3.00);

        assertEquals(money.of(5.00), m1.multiply(2.50));
    }

    @Test
    public void noNegative() {
        assertThrows(IllegalArgumentException.class, () -> money.of(-1.00));
    }
}
