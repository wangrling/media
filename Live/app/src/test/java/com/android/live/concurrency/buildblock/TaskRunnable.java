package com.android.live.concurrency.buildblock;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * Thread provides the interrupt method for interrupting a thread and for querying
 * whether a thread has been interrupted. Each thread has a boolean property
 * that represents its interrupted status; interrupting a thread sets this status.
 *
 * Sometimes you cannot throw InterruptedException , for
 * instance when your code is part of a Runnable . In these situations, you must
 * catch InterruptedException and restore the interrupted status by calling
 * interrupt on the current thread.
 */

public class TaskRunnable implements Runnable {
    BlockingQueue<File> queue;

    @Override
    public void run() {
        try {
            processFile(queue.take());
        } catch (InterruptedException e) {
            // Restore interrupted status.
            Thread.currentThread().interrupt();
        }
    }

    private void processFile(File fie) {

    }
}
