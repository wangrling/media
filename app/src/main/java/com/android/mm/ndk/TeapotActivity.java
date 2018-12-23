package com.android.mm.ndk;

import android.app.NativeActivity;

import android.view.Choreographer;

/**
 * 可以继承NativeActivity，然后在Manifest中配置相关的库。
 * 没有处理手机的输入事件，导致程序无响应。
 */

public class TeapotActivity extends NativeActivity implements
        Choreographer.FrameCallback {

    @Override
    public void doFrame(long frameTimeNanos) {
        Choreographer.getInstance().postFrameCallback(this);
        choreographerCallback(frameTimeNanos);
    }

    protected native void choreographerCallback(long frameTimeNanos);
}
