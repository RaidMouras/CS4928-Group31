package com.cafepos;

import com.cafepos.command.OrderService;
import com.cafepos.command.PayOrderCommand;
import com.cafepos.domain.Order;
import com.cafepos.payment.paymentStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PayOrderCommandTest {

    static class FakePayment implements paymentStrategy {
        boolean wasCalled = false;
        @Override
        public void pay(com.cafepos.common.Money amount) {
            wasCalled = true;
        }
    }

    @Test
    void payOrderCommandExecutesPayment() {
        Order order = new Order(4001);
        OrderService service = new OrderService(order);

        FakePayment fake = new FakePayment();
        PayOrderCommand cmd = new PayOrderCommand(service, fake, 10); // 10% tax

        cmd.execute();

        assertTrue(fake.wasCalled, "Payment strategy should have been called");
    }
}
