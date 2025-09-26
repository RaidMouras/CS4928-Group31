package com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class money implements Comparable<money> {

    private final BigDecimal amount;

    public static money of(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        return new money(BigDecimal.valueOf(value));
    }

    public static money zero() {
        return new money(BigDecimal.ZERO);
    }

    private money(BigDecimal a) {
        if (a == null)
            throw new IllegalArgumentException(" amount required");
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

    public money add(money other) {
        if (other == null)
            throw new IllegalArgumentException("some amount of money required");
        return new money(this.amount.add(other.amount));
    }

    public money multiply(int qty) {
        if (qty < 0)
            throw new IllegalArgumentException("quantity must be a positive number");
        return new money(this.amount.multiply(BigDecimal.valueOf(qty)));
    }

    public money multiply(double multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("multiplier must be >= 0");
        }
        return new money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) //check reference if true
            return true;
        if (o == null || getClass() != o.getClass())
            return false; //check null and class type
        money money = (money) o;
        return amount.compareTo(money.amount) == 0; // Compare by VALUE
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    @Override
    public int compareTo(money o) {
        return this.amount.compareTo(o.amount);
    }
}
