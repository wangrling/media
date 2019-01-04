package com.android.live.concurrency.buildblock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;


/**
 * Counting semaphores are used to control the number of activities that can access a
 * certain resource or perform a given action at the same time.
 *
 * A Semaphore manages a set of virtual permits; the initial number of permits is
 * passed to the Semaphore constructor. Activities can acquire permits (as long as
 * some remain) and release permits when they are done with them. If no permit is
 * available, acquire blocks until one is (or until interrupted or the operation times
 * out). The release method returns a permit to the semaphore.
 *
 * A degenerate of a counting semaphore is a binary semaphore, a Semaphore with an initial count
 * of one. A binary semaphore can be used as a mutex with nonreentrant locking
 * semantics; whoever holds the sole permit holds the mutex.
 *
 * 只允许bound个线程同时对HashSet进行操作。
 */

public class BoundHashSet<T> {

    private final Set<T> set;
    private final Semaphore sem;

    public BoundHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();

        boolean wasAdded = false;

        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                sem.release();
            }
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
