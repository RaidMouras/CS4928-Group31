package com.cafepos.demo;

import com.cafepos.catalog.SimpleProduct;
import com.cafepos.menu.*;
import com.cafepos.common.Money;
import com.cafepos.state.OrderFSM;
import com.cafepos.factory.ProductFactory;
import com.cafepos.command.*;
import com.cafepos.domain.*;
import java.util.Scanner;

public final class Week9Demo_MenuCLI {
    private static final ProductFactory factory = new ProductFactory();
    private static final OrderFSM fsm = new OrderFSM();
    private static Order order;
    private static OrderService service;
    private static PosRemote remote;
    private static int slot = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Initialize POS objects (reuse from Week 8)
        order = new Order(OrderIds.next());
        service = new OrderService(order);
        remote = new PosRemote(20);

        // --- Build composite menu (Part A + B) ---
        Menu root = buildMenu();

        System.out.println("═══════════════════════════════════════════════════");
        System.out.println(" Café POS System - Week 9");
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("Type one of: menu, add <recipe> <qty>, pay, state, quit");

        while (true) {
            System.out.print("💲> ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "menu" -> root.print();
                case "add" -> {
                    if (parts.length < 3) {
                        System.out.println("Usage: add <recipe> <qty>");
                        break;
                    }
                    String recipe = parts[1].toUpperCase();
                    int qty = Integer.parseInt(parts[2]);
                    Command add = new AddItemCommand(service, recipe, qty);
                    remote.setSlot(slot, add);
                    remote.press(slot++);
                }
                case "pay" -> {
                    System.out.println("[State] Paying...");
                    fsm.pay(); // NEW → PREPARING
                    fsm.markReady();
                    fsm.deliver();
                    System.out.println("Final status = " + fsm.status());
                }
                case "state" -> System.out.println("Current state = " + fsm.status());
                case "quit" -> {
                    System.out.println(" Exiting Café POS Week 9");
                    return;
                }
                default -> System.out.println("Unknown command.");
            }
        }
    }

    private static Menu buildMenu() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        Menu desserts = new Menu("Desserts");

        coffee.add(new MenuItem(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)), true));
        coffee.add(new MenuItem(new SimpleProduct("P-LAT", "Latte (Large)", Money.of(3.90)), true));

        drinks.add(coffee);

        desserts.add(new MenuItem(new SimpleProduct("P-CHS", "Cheesecake", Money.of(3.50)), false));
        desserts.add(new MenuItem(new SimpleProduct("P-OAT", "Oat Cookie", Money.of(1.20)), true));

        root.add(drinks);
        root.add(desserts);
        return root;
    }
}
