package com.cafepos.demo;




import com.cafepos.factory.ProductFactory;
import com.cafepos.menu.Menu;
import com.cafepos.menu.MenuItem;



public final class Week9Demo_Menu {
    public static void main(String[] args) {
        ProductFactory factory = new ProductFactory();

        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        Menu desserts = new Menu("Desserts");

        coffee.add(new MenuItem(factory.create("ESP"), true));
        coffee.add(new MenuItem(factory.create("LAT+L"), true));
        drinks.add(coffee);

        desserts.add(new MenuItem(factory.create("CAP"), false)); // example non-veg
        desserts.add(new MenuItem(factory.create("LAT"), true));

        root.add(drinks);
        root.add(desserts);

        System.out.println("Full menu:");
        root.print();

        System.out.println("\nVegetarian options:");
        for (MenuItem mi : root.vegetarianItems()) {
            System.out.println(" * " + mi.name() + " = " + mi.price());
        }
    }
}
