package com.google.android.camera.async;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * An {@link Executor} which posts to a {@link Handler}.
 * 执行{@link Executor#execute(Runnable)}函数实际上是执行
 * {@link Handler#post(Runnable)}函数。
 */
public class HandlerExecutor implements Executor {

    private final Handler mHandler;

    public HandlerExecutor(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void execute(Runnable runnable) {
        // Causes the Runnable r to be added to the message queue.
        // The runnable will be run on the thread to which this handler is attached.
        // 也就是运行在创建Handler的线程中。
        // 相当于Handler#sendMessageDelayed(getPostMessage(r), 0);
        mHandler.post(runnable);
    }
}
