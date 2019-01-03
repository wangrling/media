package com.android.live.camera.async;

import android.os.Handler;
import android.os.Looper;

import javax.annotation.Nonnull;

import androidx.annotation.VisibleForTesting;

import static com.google.common.base.Preconditions.checkState;

public class MainThread extends HandlerExecutor {
    private MainThread(Handler handler) {
        super(handler);
    }

    public static MainThread create() {
        return new MainThread(new Handler(Looper.getMainLooper()));
    }

    /**
     * Caches whether or not the current thread is the main thread.
     */
    private static final ThreadLocal<Boolean> sIsMainThread = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Looper.getMainLooper().getThread() == Thread.currentThread();
        }
    };

    /**
     * Asserts that the current thread is the main thread.
     */
    public static void checkMainThread() {
        checkState(sIsMainThread.get(), "Not main thread.");
    }

    /**
     * Returns true if the method is run on the main android thread.
     */
    public static boolean isMainThread() {
        return sIsMainThread.get();
    }

    /**
     * Returns a fake MainThreadExecutor which executes immediately.
     */
    @VisibleForTesting
    public static MainThread createFakeForTesting() {
        return new MainThread(null) {
            @Override
            public void execute(@Nonnull Runnable runnable) {
                //
                sIsMainThread.set(true);
                try {
                    runnable.run();
                } finally {
                    sIsMainThread.set(false);
                }
            }
        };
    }
}