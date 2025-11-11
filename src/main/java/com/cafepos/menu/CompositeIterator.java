package com.cafepos.menu;


import java.util.*;


public final class CompositeIterator implements Iterator<MenuComponent> {

    private final Deque<Iterator<MenuComponent>> stack = new ArrayDeque<>();

    public CompositeIterator(Iterator<MenuComponent> root) {
        if (root == null) throw new IllegalArgumentException("root iterator required");
        stack.push(root);
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty()) {
            Iterator<MenuComponent> top = stack.peek();
            if (top.hasNext()) return true;
            stack.pop();
        }
        return false;
    }

    @Override
    public MenuComponent next() {
        if (!hasNext()) throw new NoSuchElementException();
        Iterator<MenuComponent> it = stack.peek();
        MenuComponent comp = it.next();
        if (comp instanceof Menu m) {
            stack.push(m.childrenIterator());
        }
        return comp;
    }
}
