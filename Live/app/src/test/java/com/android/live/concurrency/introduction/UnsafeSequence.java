package com.android.live.concurrency.introduction;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * UnsafeSequence illustrates a common concurrency hazard called a race condition.
 *
 * Because threads share the same memory address space and run concurrently, they can access or
 * modify variables that other threads might be using.
 */
@NotThreadSafe
public class UnsafeSequence {
    private int value;

    /** Returns a unique value. */
    public int getNext() {
        return value++;
    }
}
