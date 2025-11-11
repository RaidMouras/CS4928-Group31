package com.cafepos;

import com.cafepos.command.*;
import com.cafepos.common.Money;
import com.cafepos.domain.Order;
import com.cafepos.payment.paymentStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PosRemoteTest {

    @Test
    void pressingButtonExecutesCommand() {
        Order order = new Order(3001);
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(2);

        remote.setSlot(0, new AddItemCommand(service, "ESP", 1));

        int before = order.items().size();
        remote.press(0);
        int after = order.items().size();

        assertEquals(before + 1, after, "Pressing the button should add an item");
    }

    @Test
    void undoReversesLastCommand() {
        Order order = new Order(3002);
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(1);

        remote.setSlot(0, new AddItemCommand(service, "LAT", 1));

        remote.press(0); // Add item
        assertEquals(1, order.items().size());

        remote.undo(); // Remove item
        assertEquals(0, order.items().size(), "Undo should remove the added item");
    }

    @Test
    void pressingUnassignedSlotDoesNothing() {
        PosRemote remote = new PosRemote(1);

        // Slot 0 is not set
        remote.press(0); // Should not throw or crash — just a message
        // No assertion needed, just ensuring stability
    }

    @Test
    void undoOnEmptyHistoryDoesNothing() {
        PosRemote remote = new PosRemote(1);

        // No command pressed, so undo should not fail
        remote.undo(); // Should print "[Remote] Nothing to undo"
        // Again, just testing it doesn't crash or misbehave
    }

    @Test
    void remoteExecutesMultipleCommandsAndSubtotalIsCorrect() {
        Order order = new Order(9001);
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);
        FakePayment payment = new FakePayment();

        remote.setSlot(0, new AddItemCommand(service, "ESP", 1)); // €2.50
        remote.setSlot(1, new AddItemCommand(service, "ESP", 1)); // €2.50
        remote.setSlot(2, new PayOrderCommand(service, payment, 10));

        remote.press(0);
        remote.press(1);
        remote.press(2);

        assertEquals(Money.of(5.00), order.subtotal(), "Subtotal should reflect 2x ESP at €2.50 each");
        assertTrue(payment.wasCalled, "Payment strategy should be triggered");
    }

    static class FakePayment implements paymentStrategy {
        boolean wasCalled = false;
        @Override
        public void pay(Money amount) {
            wasCalled = true;
        }
    }

    @Test
    void macroCommandExecutesMultipleAndUndoReversesLastOnly() {
        Order order = new Order(3003);
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(1);

        // Macro: Add 2 items
        var cmd1 = new AddItemCommand(service, "ESP", 1);
        var cmd2 = new AddItemCommand(service, "LAT", 1);
        var macro = new MacroCommand(cmd1, cmd2);

        remote.setSlot(0, macro);
        remote.press(0);

        assertEquals(2, order.items().size(), "Macro should add two items");

        remote.undo(); // should undo both in reverse order
        assertEquals(0, order.items().size(), "Undo on macro should remove both items");
    }


}
