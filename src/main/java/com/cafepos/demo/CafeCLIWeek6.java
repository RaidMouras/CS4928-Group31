package com.cafepos.demo;

import com.cafepos.catalog.Product;
import com.cafepos.factory.ProductFactory;
import com.cafepos.checkout.CheckoutService;

import com.cafepos.payment.*;
import com.cafepos.pricing.*;
import com.cafepos.common.Money;
import com.cafepos.receipt.ReceiptPrinter;

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

        // Lists for orders
        List<String> orderLines = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        // Input orders
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

        // Discount code input
        System.out.print("Enter discount code (or NONE): ");
        String code = scanner.nextLine().trim().toUpperCase();

        DiscountPolicy discountPolicy;
        switch (code) {
            case "LOYAL5":
                discountPolicy = new Loyalty5Percent();
                break;
            case "COUPON1":
                discountPolicy = new CouponFixed1();
                break;
            case "NONE":
            default:
                discountPolicy = new NoDiscount();
                code = "NONE";
        }

        // Select payment method BEFORE printing receipts
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

        // Print receipts for all order lines
        for (int i = 0; i < orderLines.size(); i++) {
            String lineRecipe = orderLines.get(i);
            int lineQty = quantities.get(i);
            String receipt = checkoutService.checkout(lineRecipe, lineQty);
            System.out.println("\n--- Receipt for Line " + (i + 1) + " ---");
            System.out.println(receipt);
        }

        // Create order for payment (you can add actual line items if desired)
        com.cafepos.domain.Order order = new com.cafepos.domain.Order(1L);



        System.out.println("\nThank you for your order!");
        scanner.close();
    }
}
