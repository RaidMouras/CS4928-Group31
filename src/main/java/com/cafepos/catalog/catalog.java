package com.cafepos.catalog;
import com.cafepos.domain.product;

import java.util.Optional;

public interface catalog {
    void add(product p);

    Optional<product> findById(String id);
}
