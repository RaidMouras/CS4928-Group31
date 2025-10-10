package com.cafepos.demo;

import com.cafepos.catalog.catalog;
import com.cafepos.catalog.inMemoryCatalog;
import com.cafepos.common.money;
import com.cafepos.domain.lineItem;
import com.cafepos.domain.order;
import com.cafepos.domain.orderIds;
import com.cafepos.domain.simpleProduct;
import com.cafepos.observer.CustomerNotifier;
import com.cafepos.observer.DeliveryDesk;
import com.cafepos.observer.KitchenDisplay;
import com.cafepos.payment.cashPayment;
import com.cafepos.payment.cardPayment;
import com.cafepos.payment.walletPayment;
import com.cafepos.payment.paymentStrategy;

import java.util.Scanner;

public final class CafeCLIWeek4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Setup product catalog with IDs and prices
        catalog catalog = new inMemoryCatalog();
        catalog.add(new simpleProduct("P1", "Cookie", money.of(1.50)));
        catalog.add(new simpleProduct("P2", "Espresso", money.of(2.50)));
        catalog.add(new simpleProduct("P3", "Brownie", money.of(2.00)));
        catalog.add(new simpleProduct("P4", "Hot Chocolate", money.of(2.80)));

        System.out.println("Welcome to the Café!");
        System.out.println("Menu:");
        System.out.println("1. Cookie- $1.50");
        System.out.println("2. Espresso- $2.50");
        System.out.println("3. Brownie- $2.00");
        System.out.println("4. Hot Chocolate- $2.80");

        System.out.print("Pick an item (1-4): ");
        int itemChoice = scanner.nextInt();

        while (itemChoice < 1 || itemChoice > 4) {
            System.out.print("Invalid choice. Please pick an item (1-4): ");
            itemChoice = scanner.nextInt();
        }

        String[] productIds = {"P1", "P2", "P3", "P4"};
        String chosenId = productIds[itemChoice - 1];

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        while (quantity < 1) {
            System.out.print("Quantity must be at least 1. Enter quantity: ");
            quantity = scanner.nextInt();
        }

        // Create a new order and register observers
        order order = new order(orderIds.next());
        order.register(new KitchenDisplay());
        order.register(new DeliveryDesk());
        order.register(new CustomerNotifier());

        // Add selected item to order
        order.addItem(new lineItem(catalog.findById(chosenId).orElseThrow(), quantity));

        // Payment selection
        System.out.println("Payment methods: 1=Cash, 2=Card, 3=Wallet");
        System.out.print("Select payment: ");
        int payType = scanner.nextInt();

        paymentStrategy strategy;
        switch (payType) {
            case 1:
                strategy = new cashPayment();
                break;
            case 2:
                strategy = new cardPayment("12345678998765432");
                break;
            case 3:
                strategy = new walletPayment("1234567891");
                break;
            default:
                System.out.println("Invalid payment method, defaulting to Cash.");
                strategy = new cashPayment();
        }

        // Pay and mark order ready (triggers observers and outputs)
        order.pay(strategy);
        order.markReady();

        System.out.println("Thank you for your order!");
        scanner.close();
    }
}
