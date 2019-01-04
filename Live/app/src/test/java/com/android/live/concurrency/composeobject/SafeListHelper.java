package com.android.live.concurrency.composeobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class SafeListHelper<E> {

    public List<E> list =
            Collections.synchronizedList(new ArrayList<E>());

    public boolean putIfAbsent(E x) {
        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent)
                list.add(x);

            return absent;
        }
    }
}
