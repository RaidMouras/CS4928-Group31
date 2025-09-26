package com.cafepos.domain;

public final class orderIds {
    private static long nextId = 1;

    public static long next() {
        return nextId++;
    }
}
