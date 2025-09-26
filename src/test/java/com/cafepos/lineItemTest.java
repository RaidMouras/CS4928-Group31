package com.cafepos;

import com.cafepos.common.money;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.product;
import com.cafepos.domain.simpleProduct;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class lineItemTest {

    @Test
    public void noNull(){
        product product = new simpleProduct("P1", "Test", money.of(1.00));

        assertThrows(IllegalArgumentException.class,
                () -> new lineItem(null, 1));
    }

    @Test
    public void no0Quantity(){
        product product = new simpleProduct("P1", "Test", money.of(1.00));
        assertThrows(IllegalArgumentException.class,
                () -> new lineItem(product, 0));
    }
}
