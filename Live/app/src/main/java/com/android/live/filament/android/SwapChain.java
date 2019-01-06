package com.android.live.filament.android;

import androidx.annotation.NonNull;

public class SwapChain {
    private final Object mSurface;
    private long mNativeObject;

    public static final long CONFIG_DEFAULT = 0x0;
    public static final long CONFIG_TRANSPARENT = 0x1;
    public static final long CONFIG_READABLE = 0x2;

    SwapChain(long nativeSwapChain, @NonNull Object surface) {
        mNativeObject = nativeSwapChain;
        mSurface = surface;
    }

    @NonNull
    public Object getNativeWindow() {
        return mSurface;
    }

    long getNativeObject() {
        if (mNativeObject == 0) {
            throw new IllegalStateException("Calling method on destroyed SwapChain");
        }
        return mNativeObject;
    }

    void clearNativeObject() {
        mNativeObject = 0;
    }
}
