package com.cafepos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import com.cafepos.menu.*;
import com.cafepos.state.*;
import com.cafepos.common.Money;

// ---- Tests for State Pattern ----

class OrderStateMachineTest {
    @Test
    void order_fsm_happy_path() {
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
    void order_cancel_from_new_and_prep() {
        OrderFSM fsm = new OrderFSM();

        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());

        // Reset and try cancelling from PREPARING
        fsm = new OrderFSM();
        fsm.pay();
        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }

    @Test
    void state_transitions_protect_invalid_actions() {
        OrderFSM fsm = new OrderFSM();

        fsm.prepare();   // no effect, should stay NEW
        assertEquals("NEW", fsm.status());

        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        fsm.deliver();   // cannot deliver before ready, should stay PREPARING
        assertEquals("PREPARING", fsm.status());

        fsm.markReady();
        assertEquals("READY", fsm.status());

        fsm.cancel();    // should do nothing, can't cancel from READY
        assertEquals("READY", fsm.status());

        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());

        // once delivered, everything else is a no-op
        fsm.pay();
        assertEquals("DELIVERED", fsm.status());
    }
}
