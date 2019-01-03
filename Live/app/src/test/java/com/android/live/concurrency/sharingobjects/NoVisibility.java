package com.android.live.concurrency.sharingobjects;

import org.junit.Test;

/**
 * In general, there is no guarantee that the reading thread will see a value written y
 * another thread on a timely basis, or even at all. In order to ensure visibility of
 * memory writes across threads, you must use synchronization.
 *
 * Because it does not use adequate synchronization, there is no guarantee that the values of
 * ready and umber written by main thread will be visible to the reader thread.
 *
 * {@link NoVisibility} could loop forever because the value of ready might never become
 * visible to the reader thread. Even more strangely, {@link NoVisibility} could print zero
 * because the write to ready might be made visile to the reader thread before the write to
 * number, a phenomenon known as reordering.
 *
 * In the absence of synchronization, the compiler, processor, and runtime can do some downright
 * weird things to the order in which operations appear to execute. Attempts to reason about
 * the order in which memory actions "must" happen in insufficiently synchronized multithreaded
 * programs will almost certainly be incorrect.
 */

public class NoVisibility {

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            // 可能输出42，可能什么也不会输出。
            System.out.println(number);
        }
    }

    @Test
    public void testNoVisibility() {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }
}
