package com.cafepos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import com.cafepos.menu.*;
import com.cafepos.common.Money;
import com.cafepos.state.*;
import com.cafepos.catalog.Product;
import com.cafepos.factory.ProductFactory;

public class Week9Tests {

    private final ProductFactory productFactory = new ProductFactory();

    // Composite + Iterator tests
    @Test
    void compositeIterator_allItemsTraversed() {
        Menu root = new Menu("Root");
        Menu drinks = new Menu("Drinks");

        Product espressoProduct = productFactory.create("ESP");
        Product latteProduct = productFactory.create("LAT");

        MenuItem espresso = new MenuItem(espressoProduct, true);
        MenuItem latte = new MenuItem(latteProduct, true);

        drinks.add(espresso);
        drinks.add(latte);
        root.add(drinks);

        List<MenuComponent> items = root.allItems();

        // Count only leaf MenuItems in items list
        long leafCount = items.stream()
                .filter(mc -> mc instanceof MenuItem)
                .count();

        assertEquals(2, leafCount);

        assertTrue(items.contains(espresso));
        assertTrue(items.contains(latte));
    }

    @Test
    void vegetarianItems_filterCorrectly() {
        Menu menu = new Menu("Menu");

        Product steakProduct = productFactory.create("STK");
        Product saladProduct = productFactory.create("SAL");
        Product pastaProduct = productFactory.create("PST");

        MenuItem steak = new MenuItem(steakProduct, false);
        MenuItem salad = new MenuItem(saladProduct, true);
        MenuItem pasta = new MenuItem(pastaProduct, true);

        menu.add(steak);
        menu.add(salad);
        menu.add(pasta);

        List<MenuItem> vegItems = menu.vegetarianItems();

        assertEquals(2, vegItems.size());
        for (MenuItem item : vegItems) {
            assertTrue(item.vegetarian());
        }
    }

    // State pattern tests

    @Test
    void stateTransition_happyPath() {
        OrderFSM fsm = new OrderFSM();

        assertEquals("NEW", fsm.status());

        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        fsm.markReady();
        assertEquals("READY", fsm.status());

        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());
    }

    @Test
    void stateTransition_cannotPrepareBeforePay() {
        OrderFSM fsm = new OrderFSM();

        assertEquals("NEW", fsm.status());
        fsm.prepare();  // Should not change the state
        assertEquals("NEW", fsm.status());
    }

    @Test
    void stateTransition_cancelNewOrder() {
        OrderFSM fsm = new OrderFSM();

        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());

        fsm.pay();
        assertEquals("CANCELLED", fsm.status());  // No change after cancel
    }
}
