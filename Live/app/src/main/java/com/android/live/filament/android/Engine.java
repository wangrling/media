package com.android.live.filament.android;

import javax.annotation.Nonnull;

public class Engine {

    private long mNativeEngine;

    @Nonnull private final TransformManager mTransformManager;

    private Engine(long nativeEngine) {
        mNativeEngine = nativeEngine;
        mTransformManager = new TransformManager(nGetTransformManager(nativeEngine));
    }


    private static native long nGetTransformManager(long nativeEngine);
}
