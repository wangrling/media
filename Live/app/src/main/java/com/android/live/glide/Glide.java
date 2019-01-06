package com.android.live.glide;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.android.live.glide.load.engine.Engine;

import javax.annotation.Nonnull;

public class Glide implements ComponentCallbacks2 {

    private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
    private static final String TAG = "Glide";

    private static volatile Glide glide;

    private static volatile boolean isInitializing;

    private final Engine engine;

    public Glide(@Nonnull Engine engine) {
        this.engine = engine;
    }


    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }
}
