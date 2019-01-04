package com.android.live.concurrency.composeobject;


import java.util.Vector;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class BetterVector<E> extends Vector<E> {

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent) {
            add(x);
        }
        return absent;
    }
}
