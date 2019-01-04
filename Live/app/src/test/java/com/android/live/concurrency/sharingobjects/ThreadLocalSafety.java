package com.android.live.concurrency.sharingobjects;

import java.sql.Connection;

import androidx.annotation.Nullable;

/**
 * A more formal means fo maintaining thread confinement is ThreadLocal, which
 * allow you to associate a per-thread value with a value-holding object.
 *
 * ThreadLocal provides get and set accessor methods that maintain a separate
 * copy of the value for each thread that uses it, so a get returns the most
 * recent value passed to set from the currently executing thread.
 *
 * ThreadLocal保证每个线程都获取/修改自己的全局变量。
 */

public class ThreadLocalSafety {

    private static ThreadLocal<Connection> connectionHolder
            = new ThreadLocal<Connection>() {
        @Nullable
        @Override
        protected Connection initialValue() {
            return super.initialValue();
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}
