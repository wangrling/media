package com.google.android.camera.async;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import static com.google.common.base.Preconditions.checkState;

// 主线程
public class MainThread extends HandlerExecutor {

    private MainThread(Handler handler) {
        super(handler);
    }

    public static MainThread create() {
        return new MainThread(new Handler(Looper.getMainLooper()));
    }

    // Caches whether or not the current thread is the main thread.
    private static final ThreadLocal<Boolean> sIsMainThread = new ThreadLocal<Boolean>() {
        @Nullable
        @Override
        protected Boolean initialValue() {
            // private static fields in classes that wish to associate state with a thread
            return Looper.getMainLooper().getThread() == Thread.currentThread();
        }
    };

    /**
     * Asserts that the current thread is the main thread.
     */
    public static void checkMainThread() {
        checkState(sIsMainThread.get(), "Not main thread.");
    }

    // Returns true if the method is run on the main android thread.
    public static boolean isMainThread() {
        return sIsMainThread.get();
    }
}
