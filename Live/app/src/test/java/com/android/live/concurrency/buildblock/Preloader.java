package com.android.live.concurrency.buildblock;

import android.content.pm.ActivityInfo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * {@link FutureTask} implements {@link Future}, which describes an abstract result-bearing
 * computation. A computation represented by a FutureTask is implemented with a Callable ,
 * the result-bearing equivalent of Runnable , and can be in one of three states: waiting to run, running,
 * or completed.
 *
 * Preloader creates a FutureTask that describes the task of loading product
 * information from a database and a thread in which the computation will be performed.
 * It provides a start method to start the thread, since it is inadvisable
 * to start a thread from a constructor or static initializer. When the program later
 * needs the ProductInfo , it can call get , which returns the loaded data if it is ready,
 * or waits for the load to complete if not.
 */

public class Preloader {

    private final FutureTask<ActivityInfo> future =
            new FutureTask<>(new Callable<ActivityInfo>() {
                @Override
                public ActivityInfo call() throws Exception {
                    return loadActivityInfo();
                }
            });

    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ActivityInfo get() throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();

            throw launderThrowable(cause);
        }
    }

    /** If the Throwable is an Error, throw it; if it is a
     * RuntimeException return it, otherwise throw IllegalStateException
     */
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        } else if (t instanceof Error) {
            throw (Error) t;
        } else
            throw new IllegalStateException("Not unchecked", t);
    }

    private ActivityInfo loadActivityInfo() {
        return null;
    }
}
