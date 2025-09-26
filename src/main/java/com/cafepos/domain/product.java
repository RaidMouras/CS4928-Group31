package com.cafepos.domain;

import com.cafepos.common.money;

public interface product {
    String id();

    String name();

    money basePrice();
}
