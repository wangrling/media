package com.android.live.concurrency.sharingobjects;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * NoVisibility demonstrated one of the ways that insufficiently synchronzied programs can
 * cause surprising results: stale data.
 *
 * {@link MutableInteger} is not thread-safe because the value field is accessed from both
 * {@link #get()} and {@link #set(int)} without synchronization.
 */

@NotThreadSafe
public class MutableInteger {
    private int value;

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}
