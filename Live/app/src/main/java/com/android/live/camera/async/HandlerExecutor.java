package com.android.live.camera.async;

import android.os.Handler;

import java.util.concurrent.Executor;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An {@link Executor} which posts to a {@link Handler}.
 */
@ParametersAreNonnullByDefault
public class HandlerExecutor implements Executor {
    private final Handler mHandler;

    public HandlerExecutor(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void execute(Runnable runnable) {
        mHandler.post(runnable);
    }
}
