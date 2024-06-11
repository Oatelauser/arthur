package com.arthur.common.utils;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * concurrent hash set collection util
 *
 * @author DearYang
 * @date 2022-07-18
 * @see ConcurrentHashMap#newKeySet()
 * @since 1.0
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> {

    private final ConcurrentHashMap.KeySetView<E, Boolean> set;

    public ConcurrentHashSet() {
        this.set = ConcurrentHashMap.newKeySet();
    }

    public ConcurrentHashSet(int initialCapacity) {
        this.set = ConcurrentHashMap.newKeySet(initialCapacity);
    }

    public ConcurrentHashSet(Set<E> other) {
        this(other.size());
        addAll(other);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return set.iterator();
    }

    @Override
    public boolean add(E o) {
        return set.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public void clear() {
        set.clear();
    }

}
