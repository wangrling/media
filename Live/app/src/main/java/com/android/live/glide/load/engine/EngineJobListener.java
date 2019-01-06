package com.android.live.glide.load.engine;

import com.android.live.glide.load.Key;

interface EngineJobListener {

    void onEngineJobComplete(EngineJob<?> engineJob, Key key, EngineResource<?> resource);
}
