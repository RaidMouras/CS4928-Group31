package com.cafepos.demo;

import com.cafepos.receipt.ReceiptPrinter;
import com.cafepos.printing.*;
import vendor.legacy.LegacyThermalPrinter;

import java.util.Scanner;

public final class cliAdapterWeek8 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Receipt Printer Demo ===");
        System.out.println("Enter receipt details (or press Enter to use default):");
        System.out.println();

        // Get order description
        System.out.print("Order description (e.g., 'Order (LAT+L) x2'): ");
        String orderDesc = scanner.nextLine();
        if (orderDesc.isEmpty()) {
            orderDesc = "Order (LAT+L) x2";
        }

        // Get subtotal
        System.out.print("Subtotal: ");
        String subtotalInput = scanner.nextLine();
        double subtotal = subtotalInput.isEmpty() ? 7.80 : Double.parseDouble(subtotalInput);

        // Get tax rate
        System.out.print("Tax rate (as percentage, e.g., 10): ");
        String taxRateInput = scanner.nextLine();
        double taxRate = taxRateInput.isEmpty() ? 10.0 : Double.parseDouble(taxRateInput);

        // Calculate tax and total
        double tax = subtotal * (taxRate / 100.0);
        double total = subtotal + tax;

        // Build receipt string
        String receipt = String.format(
                "%s%nSubtotal: %.2f%nTax (%.0f%%): %.2f%nTotal: %.2f",
                orderDesc, subtotal, taxRate, tax, total
        );

        System.out.println("\n--- Printing Receipt ---");

        // Use adapter to print
        Printer printer = new LegacyPrinterAdapter(new LegacyThermalPrinter());
        printer.print(receipt);

        System.out.println("[Demo] Sent receipt via adapter.");

        scanner.close();
    }
}
