package com.android.live.concurrency.buildblock;

import java.util.concurrent.CountDownLatch;

/**
 * 减到0表示开始，所有线程减到0表示结束。
 * It uses two latches, a “starting gate” and an “ending gate”. The starting gate is initialized
 * with a count of one; the ending gate is initialized with a count equal to the number
 * of worker threads. The first thing each worker thread does is wait on the starting
 * gate; this ensures that none of them starts working until they all are ready to start.
 * The last thing each does is count down on the ending gate; this allows the master
 * thread to wait efficiently until the last of the worker threads has finished, so it can
 * calculate the elapsed time.
 */

public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();

                        task.run();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        endGate.countDown();
                    }
                }
            };
            t.start();
        }
        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }
}
