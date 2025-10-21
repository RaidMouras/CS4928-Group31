# Week 6 Lab

Removed the **God Class** and **Long Method** smells from `OrderManagerGod` by separating pricing, tax, and receipt logic.  
**Primitive Obsession** and **Global State** were eliminated by replacing raw values (`discountCode`, `TAX_PERCENT`) with objects and injected policies.  
Applied **Extract Class**, **Introduce Interface**, and **Replace Conditional with Polymorphism** to isolate responsibilities.  
**Dependency Injection** was introduced to remove static coupling and make each component independently testable.  
Refactored design now follows SOLID: single-responsibility (each class does one thing), open/closed (new policies can be added without edits), and dependency inversion (CheckoutService depends on abstractions).  
To add a new discount type, only a new DiscountPolicy implementation and one entry in DiscountPolicies.fromCode() are needed

