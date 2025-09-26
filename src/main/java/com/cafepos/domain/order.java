package com.cafepos.domain;
import com.cafepos.common.money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class order {
    private final long id;
    private final List<lineItem> items = new ArrayList<>();

    public order(long id) { // setter
        if (id <= 0)
            throw new IllegalArgumentException("id must be positive");
        this.id = id;
    }

    public long id() { // getter
        return id;
    }

    public List<lineItem> items() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(lineItem li) {
        if (li == null)
            throw new IllegalArgumentException("line item required");
        items.add(li);
    }

    public money subtotal() {
        return items.stream().map(lineItem::lineTotal).reduce(money.zero(), money::add);
    }

    public money taxAtPercent(int percent) {
        if (percent < 0)
            throw new IllegalArgumentException("Tax percentage cannot be negative");
        double taxRate = percent / 100.0;
        return subtotal().multiply(taxRate);
    }

    public money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }
}
