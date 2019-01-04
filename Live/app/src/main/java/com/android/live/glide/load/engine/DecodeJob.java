package com.android.live.glide.load.engine;

import com.android.live.glide.load.DataSource;
import com.android.live.glide.load.Key;
import com.android.live.glide.load.data.DataFetcher;
import com.android.live.glide.load.engine.recycle.Poolable;

import androidx.annotation.NonNull;

public class DecodeJob<R> implements DataFetcherGenerator.FetchReadyCallback,
    Runnable, Comparable<DecodeJob<?>>, Poolable {

    private static final String TAG = "DecodeJob";

    private final DecodeHelper<R> decodeHelper = new DecodeHelper<>();

    private int width;
    private int height;

    DecodeJob() {

    }

    @Override
    public void reschedule() {

    }

    @Override
    public void onDataFetcherReady(Key sourceKay, Object data, DataFetcher<?> fetcher, DataSource dataSource, Key attemptedKey) {

    }

    @Override
    public void onDataFetcherFailed(Key attemptedKey, Exception e, DataFetcher<?> fetcher, DataSource dataSource) {

    }

    @Override
    public void offer() {

    }

    @Override
    public int compareTo(@NonNull DecodeJob<?> other) {
        return 0;
    }

    @Override
    public void run() {

    }
}
