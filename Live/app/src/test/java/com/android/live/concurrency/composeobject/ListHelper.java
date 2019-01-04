package com.android.live.concurrency.composeobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * ListHelper provides only the illusion of synchronization; the various list operations,
 * while all synchronized, use different locks, which means the putIfAbsent is not
 * atomic relative to modify the list while {@link #putIfAbsent(Object)} is executing.
 */

@NotThreadSafe
public class ListHelper<E> {

    public List<E> list =
            Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !list.contains(x);
        if (absent) {
            list.add(x);
        }
        return absent;
    }
}
