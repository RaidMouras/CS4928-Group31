package com.cafepos.demo;

import com.cafepos.Factory.ProductFactory;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.orderIds;
import com.cafepos.domain.product;
import com.cafepos.payment.cardPayment;
import com.cafepos.payment.cashPayment;
import com.cafepos.payment.paymentStrategy;
import com.cafepos.payment.walletPayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class CafeCLIWeek5 {
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ProductFactory factory = new ProductFactory();
    order order = new order(orderIds.next());
    List<lineItem> items = new ArrayList<>();

    System.out.println("=== Café POS System (Week 5 Interactive CLI) ===");
    System.out.println("Enter drink codes to build your order.");
    System.out.println("Drink Menu: ESP (Espresso), LAT (Latte), CAP (Cappuccino)");
    System.out.println("ESP (Espresso):    $2.50");
    System.out.println("LAT (Latte):       $3.20");
    System.out.println("CAP (Cappuccino):  $3.00");
    System.out.println("Add-ons:");
    System.out.println("+SHOT(Extra shot)");
    System.out.println("+OAT(Add oat milk)");
    System.out.println("+SYP(Add Syrup)");
    System.out.println("+L(Large size)");
    System.out.println("Example: ESP+SHOT+OAT  (Espresso with extra shot and oat milk)");
    System.out.println("Type 'done' to finish adding items.\n");

    while (true) {
        System.out.print("Enter product recipe (or 'done'): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("done")) break;

        try {
            product product = factory.create(input);
            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            lineItem item = new lineItem(product, qty);
            order.addItem(item);
            items.add(item);
            System.out.println("Added: " + product.name() + " x" + qty + "\n");

        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // Display order summary
    System.out.println("\n=== Order Summary ===");
    for (lineItem li : items) {
        System.out.println(" - " + li.product().name() + " x" + li.quantity()
                + " = " + li.lineTotal());
    }
    System.out.println("-----------------------------");
    System.out.println("Subtotal: " + order.subtotal());
    System.out.println("Tax (10%): " + order.taxAtPercent(10));
    System.out.println("Total: " + order.totalWithTax(10));

    // Simulate payment
    System.out.println("\nPayment options: cash / card / wallet");
    System.out.print("Choose payment method: ");
    String method = scanner.nextLine().trim().toLowerCase();

    paymentStrategy paymentStrategy = null;

    switch (method) {
        case "cash" -> {
            paymentStrategy = new cashPayment();
        }
        case "card" -> {
            System.out.print("Enter card number: ");
            String cardNumber = scanner.nextLine().trim();
            paymentStrategy = new cardPayment(cardNumber);
        }
        case "wallet" -> {
            System.out.print("Enter wallet ID: ");
            String walletId = scanner.nextLine().trim();
            paymentStrategy = new walletPayment(walletId);
        }
        default -> {
            System.out.println("Unsupported payment type. Defaulting to cash.");
            paymentStrategy = new cashPayment();
        }
    }

    // --- Execute Payment ---
    order.pay(paymentStrategy);
    System.out.println("\nPayment successful via " + method.toUpperCase() + ".");
    System.out.println("Thank you for your purchase!");

    System.out.println("\n=== Final Output ===");
    System.out.println("Order #" + order.id() + " processed successfully.");
    System.out.println("Total due: " + order.totalWithTax(10));
    System.out.println("Enjoy your coffee!");
    System.out.println("===============================================");
}
}
