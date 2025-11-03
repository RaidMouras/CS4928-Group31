package com.cafepos.demo;

import com.cafepos.catalog.Product;
import com.cafepos.domain.LineItem;
import com.cafepos.factory.ProductFactory;
import com.cafepos.checkout.CheckoutService;
import com.cafepos.domain.Order;
import com.cafepos.domain.LineItem;
import com.cafepos.payment.*;
import com.cafepos.pricing.*;
import com.cafepos.common.Money;
import com.cafepos.receipt.ReceiptPrinter;
import com.cafepos.observer.*;  // Import your observer classes here

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class CafeCLIWeek6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductFactory factory = new ProductFactory();

        System.out.println("=== Café POS System (Week 6 Interactive CLI) ===");
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

        List<String> orderLines = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        while (true) {
            System.out.print("Enter product recipe (or DONE to finish): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if ("DONE".equalsIgnoreCase(input)) break;
            if (input.isBlank()) {
                System.out.println("Invalid input, try again.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int qty;
            try {
                qty = Integer.parseInt(scanner.nextLine().trim());
                if (qty <= 0) {
                    System.out.println("Quantity must be positive.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity, try again.");
                continue;
            }

            orderLines.add(input);
            quantities.add(qty);
            System.out.println("Added " + input + " x" + qty);
        }

        System.out.print("Enter discount code (or NONE): ");
        String code = scanner.nextLine().trim().toUpperCase();

        DiscountPolicy discountPolicy;
        switch (code) {
            case "LOYAL5": discountPolicy = new Loyalty5Percent(); break;
            case "COUPON1": discountPolicy = new CouponFixed1(); break;
            default:
                discountPolicy = new NoDiscount();
                code = "NONE";
        }

        System.out.print("\nSelect payment method (cash/card/wallet): ");
        String method = scanner.nextLine().trim().toLowerCase();

        paymentStrategy payment;
        switch (method) {
            case "card":
                System.out.print("Enter card number: ");
                String cardNum = scanner.nextLine().trim();
                payment = new cardPayment(cardNum);
                break;
            case "wallet":
                System.out.print("Enter wallet ID or details: ");
                String walletId = scanner.nextLine().trim();
                payment = new walletPayment(walletId);
                break;
            case "cash":
            default:
                payment = new cashPayment();
        }

        PricingService pricingService = new PricingService(discountPolicy, new PercentTaxPolicy(10));
        ReceiptPrinter printer = new ReceiptPrinter();
        CheckoutService checkoutService = new CheckoutService(factory, pricingService, printer, payment, 10);

        // Create order with ID
        Order order = new Order(1L);

        // Register observers
        order.register(new KitchenDisplay());
        order.register(new CustomerNotifier());
        order.register(new DeliveryDesk());

        // Add items to order
        for (int i = 0; i < orderLines.size(); i++) {
            Product product = factory.create(orderLines.get(i));
            LineItem item = new LineItem(product, quantities.get(i));
            order.addItem(item);
        }

        // Print receipts
        for (int i = 0; i < orderLines.size(); i++) {
            String receipt = checkoutService.checkout(orderLines.get(i), quantities.get(i));
            System.out.println("\n--- Receipt for Line " + (i + 1) + " ---");
            System.out.println(receipt);
        }

        // Pay for the order
        payment.pay(order.subtotal());

        System.out.println("\nThank you for your order!");
        scanner.close();
    }
}
