package com.cafepos.menu;

import com.cafepos.common.Money;
import java.util.Iterator;


public abstract class MenuComponent {

    public void add(MenuComponent c) {
        throw new UnsupportedOperationException("add() not supported");
    }

    public void remove(MenuComponent c) {
        throw new UnsupportedOperationException("remove() not supported");
    }

    public MenuComponent getChild(int i) {
        throw new UnsupportedOperationException("getChild() not supported");
    }

    public String name() {
        throw new UnsupportedOperationException("name() not supported");
    }

    public Money price() {
        throw new UnsupportedOperationException("price() not supported");
    }

    public boolean vegetarian() {
        return false;
    }

    public Iterator<MenuComponent> iterator() {
        throw new UnsupportedOperationException("iterator() not supported");
    }

    public void print() {
        throw new UnsupportedOperationException("print() not supported");
    }
}
