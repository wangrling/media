package com.android.live.concurrency.buildblock;

import java.util.Vector;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * With a synchronized collection, these compound actions are still technically
 * thread-safe even without client-side locking, but they may no behave as you might
 * expect when other threads can concurrently modify the collection.
 */

@NotThreadSafe
public class UnsafeList {

    public static Object getLast(Vector list) {
        int lastIndex = list.size() - 1;
        return list.get(lastIndex);
    }

    public static void deleteLast(Vector list) {
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);
    }

    
}
