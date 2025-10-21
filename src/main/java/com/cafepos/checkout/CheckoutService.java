package com.cafepos.checkout;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.domain.Order;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.paymentStrategy;
import com.cafepos.pricing.PricingService;
import com.cafepos.receipt.ReceiptPrinter;


 //Replacement for OrderManagerGod.
 

public final class CheckoutService {
    private final ProductFactory factory;
    private final PricingService pricing;
    private final ReceiptPrinter printer;
    private final paymentStrategy payment;
    private final int taxPercent;

    public CheckoutService(ProductFactory factory,
                           PricingService pricing,
                           ReceiptPrinter printer,
                           paymentStrategy payment,
                           int taxPercent) {
        if (factory == null || pricing == null || printer == null || payment == null)
            throw new IllegalArgumentException("dependencies required");
        this.factory = factory;
        this.pricing = pricing;
        this.printer = printer;
        this.payment = payment;
        this.taxPercent = taxPercent;
    }


    public String checkout(String recipe, int qty) {
        if (recipe == null || recipe.isBlank()) throw new IllegalArgumentException("recipe required");
        if (qty <= 0) qty = 1;


        Product product = factory.create(recipe);
        Money unit = (product instanceof com.cafepos.decorator.Priced p)
                ? p.price() : product.basePrice();
        Money subtotal = unit.multiply(qty);

      //Delegate pricing
        var result = pricing.price(subtotal);

        // Handle payment (week3)
        payment.pay(result.total());

        return printer.format(recipe, qty, result, taxPercent);
    }

   
    public String checkoutOrder(Order order, String recipe, int qty) {
        if (order == null) throw new IllegalArgumentException("order required");
        String receipt = checkout(recipe, qty);
        order.pay(payment); 
        return receipt;
    }
}
