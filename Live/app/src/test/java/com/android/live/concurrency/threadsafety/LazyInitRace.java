package com.android.live.concurrency.threadsafety;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * LazyInitRace has race conditions that can undermine its correctness.
 * Say that threads A and B execute {@link #getInstance} at the same time.
 *
 * To avoid race conditions, there must be a way to prevent other can ensure that
 * threads can observe or modify the state only before we start or after we finish,
 * but not in middle.
 */
@NotThreadSafe
public class LazyInitRace {

    private Object instance = null;

    public Object getInstance() {
        if (instance == null) {
            instance = new Object();
        }
        return instance;
    }
}
