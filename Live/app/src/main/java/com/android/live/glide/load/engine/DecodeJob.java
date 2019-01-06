package com.android.live.glide.load.engine;

import com.android.live.glide.load.DataSource;
import com.android.live.glide.load.Key;
import com.android.live.glide.load.ResourceEncoder;
import com.android.live.glide.load.data.DataFetcher;
import com.android.live.glide.load.engine.cache.DiskCache;
import com.android.live.glide.load.engine.recycle.Poolable;
import com.android.live.glide.util.pool.StateVerifier;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pools;

/**
 * A class responsible fro decoding resources either from cached data of from the original source
 * and applying transformation and transcodes.
 *
 * <p>Note: This class has a natural ordering that is inconsistent with equals.
 *
 * @param <R>   The type of resource that will be transcoded from the decoded and transformed
 *           resource.
 */


public class DecodeJob<R> implements DataFetcherGenerator.FetchReadyCallback,
    Runnable, Comparable<DecodeJob<?>>, Poolable {

    private static final String TAG = "DecodeJob";

    private final DecodeHelper<R> decodeHelper = new DecodeHelper<>();
    private final List<Throwable> throwables = new ArrayList<>();
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    private final DiskCacheProvider diskCacheProvider;
    private final Pools.Pool<DecodeJob<?>> pool;
    private final DeferredEncodeManager<?> deferredEncodeManager = new DeferredEncodeManager<>();


    private int width;
    private int height;

    DecodeJob(DiskCacheProvider diskCacheProvider, Pools.Pool<DecodeJob<?>> pool) {
        this.diskCacheProvider = diskCacheProvider;
        this.pool = pool;
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

    /**
     * Allows transformed resources to be encoded after the transcoded result is already delivered to
     * requestors.
     */
    private static class DeferredEncodeManager<Z> {
        private Key key;
        private ResourceEncoder<Z> encoder;

    }

    interface DiskCacheProvider {
        DiskCache getDiskCache();
    }
}
