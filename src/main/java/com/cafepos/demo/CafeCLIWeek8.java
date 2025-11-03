package com.cafepos.demo;

import com.cafepos.domain.*;
import com.cafepos.payment.*;
import com.cafepos.command.*;
import com.cafepos.factory.ProductFactory;
import com.cafepos.checkout.CheckoutService;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;
import com.cafepos.observer.*;
import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import java.util.Scanner;
import java.math.BigDecimal;

public final class CafeCLIWeek8 {

    // Week 8 Command Pattern components
    private static Order order;
    private static OrderService service;
    private static PosRemote remote;
    private static int commandSlot = 0;

    // Week 6 components
    private static DiscountPolicy discountPolicy = new NoDiscount();
    private static String discountCode = "NONE";
    private static ProductFactory factory = new ProductFactory();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize Week 8 components
        order = new Order(OrderIds.next());
        service = new OrderService(order);
        remote = new PosRemote(20);  // Support up to 20 command slots

        // Register Week 4 Observers
        order.register(new KitchenDisplay());
        order.register(new CustomerNotifier());
        order.register(new DeliveryDesk());

        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   Café POS System - Week 8 Enhanced (Command Pattern)         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("\n🍵 DRINK RECIPES:");
        System.out.println("  ESP           → Espresso");
        System.out.println("  LAT           → Latte");
        System.out.println("  CAP           → Cappuccino");
        System.out.println("\n✨ ADD-ONS:");
        System.out.println("  +SHOT         → Extra Shot");
        System.out.println("  +OAT          → Oat Milk");
        System.out.println("  +SYP          → Syrup");
        System.out.println("  +L            → Large Size");
        System.out.println("\n💡 Example: ESP+SHOT+OAT (Espresso with extra shot and oat milk)");
        System.out.println("\n📋 COMMANDS:");
        System.out.println("  add <recipe> <qty>     → Add item to order (creates Command)");
        System.out.println("  discount <code>        → Apply discount (LOYAL5, COUPON1)");
        System.out.println("  view                   → View current order");
        System.out.println("  receipt                → Print detailed receipt with pricing");
        System.out.println("  undo                   → Undo last add action");
        System.out.println("  pay <method>           → Pay (cash/card/wallet)");
        System.out.println("  quit                   → Exit");
        System.out.println("═══════════════════════════════════════════════════════════════\n");

        while (true) {
            System.out.print("💲 Enter command: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "add":
                    handleAdd(scanner, parts);
                    break;

                case "discount":
                    handleDiscount(scanner, parts);
                    break;

                case "undo":
                    remote.undo();
                    break;

                case "view":
                    handleView();
                    break;

                case "receipt":
                    handleReceipt();
                    break;

                case "pay":
                    if (handlePay(scanner, parts)) {
                        scanner.close();
                        return;  // Exit after successful payment
                    }
                    break;

                case "quit":
                case "exit":
                    System.out.println("👋 Thank you for using Café POS. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("❌ Unknown command: " + command);
                    System.out.println("   Type: add, discount, view, receipt, undo, pay, quit");
            }
        }
    }

    private static void handleAdd(Scanner scanner, String[] parts) {
        if (parts.length < 3) {
            System.out.println("❌ Usage: add <recipe> <qty>");
            System.out.println("   Example: add ESP+SHOT+OAT 1");
            return;
        }

        String recipe = parts[1].toUpperCase();
        int qty;
        try {
            qty = Integer.parseInt(parts[2]);
            if (qty <= 0) {
                System.out.println("❌ Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid quantity. Please enter a number.");
            return;
        }

        // Create and execute AddItemCommand (Week 8 Command Pattern)
        Command addCmd = new AddItemCommand(service, recipe, qty);
        remote.setSlot(commandSlot, addCmd);
        remote.press(commandSlot);
        commandSlot++;
    }

    private static void handleDiscount(Scanner scanner, String[] parts) {
        if (parts.length < 2) {
            System.out.print("Enter discount code (LOYAL5/COUPON1/NONE): ");
            String code = scanner.nextLine().trim().toUpperCase();
            applyDiscount(code);
        } else {
            applyDiscount(parts[1].toUpperCase());
        }
    }

    private static void applyDiscount(String code) {
        switch (code) {
            case "LOYAL5":
                discountPolicy = new Loyalty5Percent();
                discountCode = "LOYAL5";
                System.out.println("✅ Applied LOYAL5: 5% loyalty discount");
                break;
            case "COUPON1":
                discountPolicy = new CouponFixed1();
                discountCode = "COUPON1";
                System.out.println("✅ Applied COUPON1: €1.00 fixed discount");
                break;
            case "NONE":
                discountPolicy = new NoDiscount();
                discountCode = "NONE";
                System.out.println("✅ No discount applied");
                break;
            default:
                System.out.println("❌ Invalid discount code. Available: LOYAL5, COUPON1, NONE");
        }
    }

    private static void handleView() {
        if (order.items().isEmpty()) {
            System.out.println("📭 Order is empty.");
            return;
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     CURRENT ORDER                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        int itemNum = 1;
        for (var item : order.items()) {
            System.out.printf("  %d. %s x%d @ %s%n",
                    itemNum++,
                    item.product().name(),
                    item.quantity(),
                    item.product().basePrice());
        }

        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.println("  Subtotal:        " + order.subtotal());
        System.out.println("  Discount Code:   " + discountCode);

        // CORRECTED: Use discountOf() instead of applyDiscount()
        Money discount = discountPolicy.discount(order.subtotal());

        // CORRECTED: Use asBigDecimal().subtract() for Money operations
        Money afterDiscount = Money.of(
                order.subtotal().asBigDecimal().subtract(discount.asBigDecimal())
        );

        // CORRECTED: Check if Money is non-zero using asBigDecimal().signum()
        if (discount.asBigDecimal().signum() > 0) {
            System.out.println("  Discount:       -" + discount);
            System.out.println("  After Discount:  " + afterDiscount);
        }

        Money taxAmount = Money.of(
                order.subtotal().asBigDecimal()
                        .multiply(BigDecimal.valueOf(10))
                        .divide(BigDecimal.valueOf(100))
        );

        Money total = Money.of(
                afterDiscount.asBigDecimal().add(taxAmount.asBigDecimal())
        );

        System.out.println("  Tax (10%):       " + taxAmount);
        System.out.println("  TOTAL:           " + total);
        System.out.println("═══════════════════════════════════════════════════════════════\n");
    }

    private static void handleReceipt() {
        if (order.items().isEmpty()) {
            System.out.println("❌ Cannot print receipt for empty order.");
            return;
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                       RECEIPT                                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Money totalSubtotal = Money.of(BigDecimal.ZERO);
        Money totalDiscount = Money.of(BigDecimal.ZERO);
        Money totalTax = Money.of(BigDecimal.ZERO);

        for (var item : order.items()) {
            System.out.println("\n--- Item Receipt ---");

            Money unitPrice = item.product().basePrice();
            Money subtotal = Money.of(
                    unitPrice.asBigDecimal().multiply(BigDecimal.valueOf(item.quantity()))
            );


            Money itemDiscount = discountPolicy.discount(subtotal);


            Money discounted = Money.of(
                    subtotal.asBigDecimal().subtract(itemDiscount.asBigDecimal())
            );

            Money itemTax = Money.of(
                    discounted.asBigDecimal()
                            .multiply(BigDecimal.valueOf(10))
                            .divide(BigDecimal.valueOf(100))
            );

            Money itemTotal = Money.of(
                    discounted.asBigDecimal().add(itemTax.asBigDecimal())
            );

            System.out.println(item.product().name() + " x" + item.quantity());
            System.out.println("Unit Price: " + unitPrice);
            System.out.println("Subtotal: " + subtotal);

            // CORRECTED: Check if discount exists
            if (itemDiscount.asBigDecimal().signum() > 0) {
                System.out.println("Discount: -" + itemDiscount);
            }

            System.out.println("Tax (10%): " + itemTax);
            System.out.println("Item Total: " + itemTotal);

            // Accumulate totals
            totalSubtotal = Money.of(totalSubtotal.asBigDecimal().add(subtotal.asBigDecimal()));
            totalDiscount = Money.of(totalDiscount.asBigDecimal().add(itemDiscount.asBigDecimal()));
            totalTax = Money.of(totalTax.asBigDecimal().add(itemTax.asBigDecimal()));
        }

        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("Order Total Summary:");
        System.out.println("Total Subtotal: " + totalSubtotal);
        if (totalDiscount.asBigDecimal().signum() > 0) {
            System.out.println("Total Discount: -" + totalDiscount);
        }
        System.out.println("Total Tax: " + totalTax);

        Money finalTotal = Money.of(
                totalSubtotal.asBigDecimal()
                        .subtract(totalDiscount.asBigDecimal())
                        .add(totalTax.asBigDecimal())
        );

        System.out.println("GRAND TOTAL: " + finalTotal);
        System.out.println("═══════════════════════════════════════════════════════════════\n");
    }

    private static boolean handlePay(Scanner scanner, String[] parts) {
        if (order.items().isEmpty()) {
            System.out.println("❌ Cannot pay for an empty order.");
            return false;
        }

        if (parts.length < 2) {
            System.out.println("❌ Usage: pay <method> (cash/card/wallet)");
            System.out.println("   For card: pay card <cardnumber>");
            System.out.println("   For wallet: pay wallet <walletid>");
            return false;
        }

        String method = parts[1].toLowerCase();
        paymentStrategy payment = null;

        try {
            switch (method) {
                case "cash":
                    payment = new cashPayment();
                    break;

                case "card":
                    String cardNum;
                    if (parts.length >= 3) {
                        cardNum = parts[2];
                    } else {
                        System.out.print("Enter card number: ");
                        cardNum = scanner.nextLine().trim();
                    }
                    payment = new cardPayment(cardNum);
                    break;

                case "wallet":
                    String walletId;
                    if (parts.length >= 3) {
                        walletId = parts[2];
                    } else {
                        System.out.print("Enter wallet ID: ");
                        walletId = scanner.nextLine().trim();
                    }
                    payment = new walletPayment(walletId);
                    break;

                default:
                    System.out.println("❌ Invalid payment method. Use: cash, card, or wallet");
                    return false;
            }

            if (payment != null) {
                // Week 8: Create and execute PayOrderCommand
                Command payCmd = new PayOrderCommand(service, payment, 10);
                remote.setSlot(commandSlot, payCmd);
                remote.press(commandSlot);

                // Calculate final total
                Money discount = discountPolicy.discount(order.subtotal());
                Money afterDiscount = Money.of(
                        order.subtotal().asBigDecimal().subtract(discount.asBigDecimal())
                );
                Money taxAmount = Money.of(
                        afterDiscount.asBigDecimal()
                                .multiply(BigDecimal.valueOf(10))
                                .divide(BigDecimal.valueOf(100))
                );
                Money finalTotal = Money.of(
                        afterDiscount.asBigDecimal().add(taxAmount.asBigDecimal())
                );

                System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
                System.out.println("║              ✅ ORDER COMPLETED SUCCESSFULLY!                  ║");
                System.out.println("╚═══════════════════════════════════════════════════════════════╝");
                System.out.println("  Order ID: " + order.id());
                System.out.println("  Total Paid: " + finalTotal);
                System.out.println("  Payment: " + method.toUpperCase());
                System.out.println("  Discount: " + discountCode);
                System.out.println("\n  🎉 Thank you for your purchase!");
                System.out.println("═══════════════════════════════════════════════════════════════\n");
                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Error processing payment: " + e.getMessage());
        }

        return false;
    }
}
