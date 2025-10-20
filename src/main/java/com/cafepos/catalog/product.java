package com.cafepos.catalog;

import com.cafepos.common.money;

public interface product {
    String id();

    String name();

    money basePrice();
}
