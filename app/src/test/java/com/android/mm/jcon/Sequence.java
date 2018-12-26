package com.android.mm.jcon;

import org.junit.runner.notification.RunListener;

import androidx.annotation.GuardedBy;

@RunListener.ThreadSafe
public class Sequence {
    @GuardedBy("this") private int value;

    public synchronized int getNext() {
        return value++;
    }
}
