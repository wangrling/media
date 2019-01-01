package com.google.android.camera.util;

import android.content.Context;

import androidx.annotation.Nullable;

/**
 * Initializable singleton for providing the application level context
 * object instead of initializing each singleton separately.
 */
public class AndroidContext {

    private static AndroidContext sInstance;

    /**
     * The android context object cannot be created until the andorid
     * has create the application object. The androidContext object
     * must be initialized before other singletons can use it.
     */
    public static void initialize(@Nullable Context context) {
        if (sInstance == null) {
            sInstance = new AndroidContext(context);
        }
    }

    /**
     * Returns a previously initialized instance, throw if it has not been
     * initialized yet.
     */
    public static AndroidContext instance() {
        // 确保Context已经被赋值。
        if (sInstance == null) {
            throw new IllegalStateException("Android context was not initialized.");
        }
        return sInstance;
    }

    private final Context mContext;
    private AndroidContext(Context context) {
        mContext = context;
    }

    public Context get() {
        return mContext;
    }
}
