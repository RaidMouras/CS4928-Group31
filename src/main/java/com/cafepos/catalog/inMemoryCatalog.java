package com.cafepos.catalog;
import com.cafepos.domain.product;

import java.util.*;

public final class inMemoryCatalog implements catalog {
    private final Map<String, product> byId = new HashMap<>();

    @Override
    public void add(product p) {
        if (p == null)
            throw new IllegalArgumentException("product required");
        byId.put(p.id(), p);
    }

    @Override
    public Optional<product> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }
}