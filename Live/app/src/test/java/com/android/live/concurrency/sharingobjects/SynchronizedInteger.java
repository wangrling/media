package com.android.live.concurrency.sharingobjects;

import javax.annotation.concurrent.GuardedBy;

/**
 * Among other hazards, it is susceptible to stale values: if one thread calls {@link #set(int)},
 * other threads calling {@link #get()} may or may not see that update.
 *
 * Synchronizing only the setter would not be sufficient: threads calling get would still
 * be able to see stale values.
 *
 * Thus, even if you don't care about stale values, it is not safe to use shared mutable long
 * and double variables in multithreaded programs unless they are declared volatile or guarded
 * by a lock.
 *
 * When thread A executes a synchronized block, and subsequently thread B enters a synchronized block
 * guarded by the same ock, the values of variables that were visible to A prior to releasing the
 * lock are guaranteed to be visible to B upon acquiring the lock.
 *
 * Locking is not just about mutual exclusion exclusion; it is also about memory visibility. To
 * ensure that all threads see the most up-to-date values of shared mutable variables, the reading
 * and writing must synchronize on a common lock.
 */

public class SynchronizedInteger {

    @GuardedBy("this") private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
    }
}
