package com.cafepos.demo;

import com.cafepos.checkout.CheckoutService;
import com.cafepos.Factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.payment.cashPayment;
import com.cafepos.payment.cardPayment;
import com.cafepos.payment.walletPayment;
import com.cafepos.payment.paymentStrategy;
import com.cafepos.common.money;

import java.math.BigDecimal;
import java.util.Scanner;

public final class CafeCLIWeek6 {

    private static void printMenu() {
        System.out.println("\n=== Cafe Menu ===");
        System.out.println("Base Coffee Codes and Prices:");
        System.out.println("ESP  - Espresso          $3.00");
        System.out.println("LAT  - Latte             $3.50");
        System.out.println("CAP  - Cappuccino        $3.75");
        System.out.println("BREW - Brewed Coffee     $2.50");
        System.out.println("\nAdd-ons:");
        System.out.println("SHOT  - Extra Shot        $0.75");
        System.out.println("OAT  - Oat Milk          $0.50");
        System.out.println("SYP  - Syrup             $0.40");
        System.out.println("L  - Size Large        $1.00");
        System.out.println("======================");
        System.out.println("Combine codes to form recipes (e.g. LAT + SHOT)");
    }

    private static int promptQuantity(Scanner scanner) {
        int quantity = 1;
        while (true) {
            System.out.println("Enter quantity (integer > 0):");
            String qtyInput = scanner.nextLine().trim();
            try {
                quantity = Integer.parseInt(qtyInput);
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than zero. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer quantity.");
            }
        }
        return quantity;
    }

    private static paymentStrategy promptPaymentMethod(Scanner scanner) {
        System.out.println("\nPayment options: cash / card / wallet");
        System.out.print("Choose payment method: ");
        String method = scanner.nextLine().trim().toLowerCase();

        paymentStrategy paymentStrategy = null;

        switch (method) {
            case "cash" -> paymentStrategy = new cashPayment();
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
        return paymentStrategy;
    }

    private static String promptDiscountCode(Scanner scanner) {
        System.out.println("Enter discount code (e.g., LOYAL5 for 5% off, SAVE10 for $3 off, blank for none):");
        return scanner.nextLine().trim();
    }

    private static DiscountPolicy selectDiscountPolicy(String code) {
        if (code.equalsIgnoreCase("LOYAL5")) {
            return new LoyaltyPercentDiscount(5);
        } else if (code.equalsIgnoreCase("SAVE3")) {
            return new FixedCouponDiscount(money.of(BigDecimal.valueOf(3.0)));
        } else {
            return new NoDiscount();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Cafe POS Week 6 CLI");
        printMenu();

        boolean running = true;
        while (running) {
            System.out.println("\nEnter recipe code (or 'exit' to quit):");
            String recipe = scanner.nextLine().trim();
            if (recipe.equalsIgnoreCase("exit")) {
                running = false;
                continue;
            }

            int quantity = promptQuantity(scanner);
            paymentStrategy payment = promptPaymentMethod(scanner);
            String discountCode = promptDiscountCode(scanner);

            // Select DiscountPolicy separate from recipe/factory
            DiscountPolicy discountPolicy = selectDiscountPolicy(discountCode);
            TaxPolicy taxPolicy = new FixedRateTaxPolicy(10);
            PricingService pricingService = new PricingService(discountPolicy, taxPolicy);
            ReceiptPrinter receiptPrinter = new ReceiptPrinter();
            ProductFactory productFactory = new ProductFactory();
            CheckoutService checkoutService = new CheckoutService(productFactory, pricingService, receiptPrinter, 10);

            // Pass only recipe to checkout, discount code handled separately
            String receipt = checkoutService.checkout(recipe, quantity);
            System.out.println("\n------ Receipt ------");
            System.out.println(receipt);
            System.out.println("---------------------");
            System.out.println("Payment method used: " + payment.getClass().getSimpleName());
            if (!discountCode.isEmpty()) {
                System.out.println("Discount code applied: " + discountCode);
            } else {
                System.out.println("No discount code applied.");
            }
        }
        scanner.close();
        System.out.println("Thank you for using Cafe POS CLI Week 6!");
    }
}
