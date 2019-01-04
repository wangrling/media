package com.android.live.concurrency.composeobject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * if a class composed of multiple independent thread-safe state variables and has no
 * operations that have any invalid state transitions, then it can delegate thread safety
 * to the underlying state variables.
 */

public class NumberRange {

    // INVARIANT: lower <= upper;

    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        // Warning -- unsafe check-then-act
        if (i > upper.get())
            throw new IllegalArgumentException(
                    "can’t set lower to " + i + " > upper");
        lower.set(i);
    }

    public void setUpper(int i) {
        // Warning -- unsafe check-then-act
        if (i < lower.get()) {
            throw new IllegalArgumentException(
                    "can’t set upper to " + i + " < lower");
        }
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}
