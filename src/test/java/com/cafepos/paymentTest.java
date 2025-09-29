package com.cafepos;

import com.cafepos.common.money;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.simpleProduct;
import com.cafepos.payment.cardPayment;
import com.cafepos.payment.cashPayment;
import com.cafepos.payment.paymentStrategy;
import com.cafepos.payment.walletPayment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class paymentTest {

    // Helper to capture System.out for assertion
    private String captureStdout(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        try {
            action.run();
        } finally {
            System.setOut(original);
            ps.close();
        }
        return baos.toString().trim();
    }

    @Test
    void payment_strategy_called() {
        var product = new simpleProduct("Coffee", "Freshly brewed", money.of(3.50));
        var order = new order(1);
        order.addItem(new lineItem(product, 1));

        fakePaymentStrategy fake = new fakePaymentStrategy();
        order.pay(fake);

        assertTrue(fake.called, "Payment strategy should be called when paying an order");
    }


    @Test
    public void cash_payment_prints_expected_message() {
        var product = new simpleProduct("Tea", "Green tea", money.of(2.50));
        var order = new order(2);
        order.addItem(new lineItem(product, 2)); // any simple order

        String out = captureStdout(() -> order.pay(new cashPayment()));

        assertTrue(out.contains("[Cash]"), "Should tag output as [Cash]");
        assertTrue(out.contains("Customer paid"), "Should state customer paid");
        assertTrue(out.contains("EUR"), "Should include currency label");
    }

    @Test
    public void card_payment_prints_expected_message_with_masked_digits() {
        var product = new simpleProduct("Muffin", "Blueberry", money.of(3.00));
        var order = new order(3);
        order.addItem(new lineItem(product, 1));

        String cardNumber = "1234567812345678";
        String out = captureStdout(() -> order.pay(new cardPayment(cardNumber)));

        assertTrue(out.contains("[Card]"), "Should tag output as [Card]");
        assertTrue(out.contains("Customer paid"), "Should state customer paid");
        assertTrue(out.contains("EUR"), "Should include currency label");
        assertTrue(out.contains("****5678"), "Should mask and show last 4 digits");
    }

    @Test
    public void wallet_payment_prints_expected_message_with_wallet_id() {
        var product = new simpleProduct("Sandwich", "Ham & cheese", money.of(4.00));
        var order = new order(4);
        order.addItem(new lineItem(product, 1));

        String walletId = "wallet-xyz";
        String out = captureStdout(() -> order.pay(new walletPayment(walletId)));

        assertTrue(out.contains("[Wallet]"), "Should tag output as [Wallet]");
        assertTrue(out.contains("Customer paid"), "Should state customer paid");
        assertTrue(out.contains("EUR"), "Should include currency label");
        assertTrue(out.contains(walletId), "Should include the wallet ID");
    }
}
