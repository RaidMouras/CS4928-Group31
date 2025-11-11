package com.cafepos.demo;

import com.cafepos.menu.*;
import com.cafepos.common.Money;
import java.util.Scanner;

public final class Week9Demo_MenuCLI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        Menu desserts = new Menu("Desserts");

        coffee.add(new MenuItem(
                new com.cafepos.catalog.SimpleProduct("P-ESP", "Espresso", Money.of(2.50)), true));
        coffee.add(new MenuItem(
                new com.cafepos.catalog.SimpleProduct("P-LAT", "Latte (Large)", Money.of(3.90)), true));

        drinks.add(coffee);

        desserts.add(new MenuItem(
                new com.cafepos.catalog.SimpleProduct("P-CHS", "Cheesecake", Money.of(3.50)), false));
        desserts.add(new MenuItem(
                new com.cafepos.catalog.SimpleProduct("P-OAT", "Oat Cookie", Money.of(1.20)), true));

        root.add(drinks);
        root.add(desserts);

        while (true) {
            System.out.println("\n--- Café POS Menu CLI ---");
            System.out.println("1. View full menu");
            System.out.println("2. Show vegetarian items");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String input = sc.nextLine();

            switch (input) {
                case "1" -> root.print();
                case "2" -> {
                    System.out.println("Vegetarian options:");
                    for (MenuItem mi : root.vegetarianItems()) {
                        System.out.println(" * " + mi.name() + " = " + mi.price());
                    }
                }
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
