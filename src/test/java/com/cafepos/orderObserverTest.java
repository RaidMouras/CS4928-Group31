package com.cafepos;

import com.cafepos.catalog.product;
import com.cafepos.common.money;
import com.cafepos.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class orderObserverTest {

    @Test
    void singleObserverReceivesItemAddedEvent() {
        order order = new order(1001);
        product product = new simpleProduct("P-01", "TestProduct", money.of(5.00));

        fakeObserver fake = new fakeObserver();
        order.register(fake);

        order.addItem(new lineItem(product, 1));

        assertTrue(fake.hasEvent("itemAdded"), "Expected 'itemAdded' event to be received.");
    }

    @Test
    void multipleObserversReceiveReadyEvent() {
        order order = new order(1002);

        fakeObserver obs1 = new fakeObserver();
        fakeObserver obs2 = new fakeObserver();

        order.register(obs1);
        order.register(obs2);

        order.markReady();

        assertTrue(obs1.hasEvent("ready"), "Observer 1 should receive 'ready' event.");
        assertTrue(obs2.hasEvent("ready"), "Observer 2 should receive 'ready' event.");
    }

    @Test
    void observerCanBeUnregistered() {
        order order = new order(1003);
        fakeObserver observer = new fakeObserver();

        order.register(observer);
        order.unregister(observer);

        order.markReady();

        assertFalse(observer.hasEvent("ready"), "Observer should not receive event after being unregistered.");
    }

    @Test
    void noDuplicateRegistration() {
        order order = new order(1004);
        product product = new simpleProduct("P-02", "Tea", money.of(3.00));

        fakeObserver observer = new fakeObserver();

        order.register(observer);
        order.register(observer); // try to register twice

        order.addItem(new lineItem(product, 1));

        List<String> receivedEvents = observer.getReceivedEvents();
        long count = receivedEvents.stream().filter(e -> e.equals("itemAdded")).count();

        assertEquals(1, count, "Observer should only receive one 'itemAdded' event.");
    }
}
