package com.android.mm.jcon;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class UnsafeSequence {
    private int value;

    // Returns a unique value.
    public int getNext() {
        return value++;
    }
}
