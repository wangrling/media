package com.android.live.concurrency.sharingobjects;

/**
 * Locking can guarantee both visibility and atomicity; volatile variables can only guarantee
 * visibility.
 *
 * Use volatile variables only when they simplify implementing and verifying you synchronization
 * policy; avoid using volatile variables when verifying correctness would require subtitle reasoning
 * about visibility.
 *
 * Good uses of volatile variables include ensuring the visibility of their own state, that of the
 * object they refer to, or indicating that an important lifecycle event (such as initialization or
 * shutdown) has occurred.
 *
 * Te most common use for volatile variables is as a completion, interruption, or status flag, such
 * as the {@link #asleep} flag.
 */

public class VolatileVariable {

    volatile boolean asleep;

    public void doSomething() {
        while (!asleep) {
            Thread.yield();
        }
    }
}
