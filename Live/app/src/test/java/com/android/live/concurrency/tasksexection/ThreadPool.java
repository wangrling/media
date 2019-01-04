package com.android.live.concurrency.tasksexection;

import java.util.concurrent.Executors;

/**
 * {@link Executors#newFixedThreadPool(int)}
 * A fixed-size thread pool creates threads as tasks are submitted,
 * up to the maximum pool size, and then attempts to keep the pool
 * size constant (adding new threads if a thread dies due to an unexpected
 * Exception ).
 *
 * {@link Executors#newCachedThreadPool()}
 * A cached thread pool has more flexibility to reap idle
 * threads when the current size of the pool exceeds the demand for process-
 * ing, and to add new threads when demand increases, but places no bounds
 * on the size of the pool.
 *
 * {@link Executors#newSingleThreadExecutor()}
 * A single-threaded executor creates a single worker
 * thread to process tasks, replacing it if it dies unexpectedly. Tasks are
 * guaranteed to be processed sequentially according to the order imposed by the
 * task queue (FIFO, LIFO, priority order).
 *
 * {@link Executors#newScheduledThreadPool(int)}
 * A fixed-size thread pool that supports delayed and
 * periodic task execution, similar to Timer.
 */

public class ThreadPool {
}
