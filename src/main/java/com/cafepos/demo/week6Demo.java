package com.cafepos.demo;

import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;
import com.cafepos.payment.paymentStrategy;
import com.cafepos.checkout.CheckoutService;


public final class week6Demo {
    public static void main(String[] args) {

        // Old behavior (smelly)
        String oldReceipt = com.cafepos.smells.OrderManagerGod.process(
                "LAT+L", 2, "CARD", "LOYAL5", false
        );

        // New behaviour
        DiscountPolicy discountPolicy = DiscountPolicies.fromCode("LOYAL5");
        TaxPolicy taxPolicy = new PercentTaxPolicy(10);
        PricingService pricing = new PricingService(discountPolicy, taxPolicy);
        ReceiptPrinter printer = new ReceiptPrinter();
        ProductFactory factory = new ProductFactory();

        
        paymentStrategy cardPayment = total ->
                System.out.println("[Card] Customer paid " + total + " EUR with card ****1234");

        CheckoutService checkout = new CheckoutService(
                factory,
                pricing,
                printer,
                cardPayment,
                10
        );

        //Perform checkout and compare output
        String newReceipt = checkout.checkout("LAT+L", 2);

        System.out.println("Old Receipt:\n" + oldReceipt);
        System.out.println("\nNew Receipt:\n" + newReceipt);
        System.out.println("\nMatch: " + oldReceipt.equals(newReceipt));
    }
}
