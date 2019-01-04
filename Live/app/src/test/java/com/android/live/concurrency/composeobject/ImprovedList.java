package com.android.live.concurrency.composeobject;

import java.util.ArrayList;
import java.util.List;

/**
 * ImprovedList adds an additional level of locking using its own intrinsic lock.
 * It doesn't care whether the underlying List is thread-safe. Because it provides
 * its own consistent locking that provides thread safety even if the List is not
 * thread-safe or changes its locking implementation.
 */
public class ImprovedList<T> extends ArrayList<T> {

    private final List<T> list;

    public ImprovedList(List<T> list) {
        this.list = list;
    }

    public synchronized boolean putIfAbsent(T x) {
        boolean contains = list.contains(x);
        if (contains)
            list.add(x);
        return !contains;
    }

    public synchronized void clear() {
        list.clear();
    }
}
