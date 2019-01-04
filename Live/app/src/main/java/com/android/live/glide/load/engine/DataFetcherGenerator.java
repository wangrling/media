package com.android.live.glide.load.engine;

import com.android.live.glide.load.DataSource;
import com.android.live.glide.load.Key;
import com.android.live.glide.load.data.DataFetcher;

/**
 * Generate a series of {@link DataFetcher} using registered {@link ModelLoader} and a model
 */

public interface DataFetcherGenerator {

    interface FetchReadyCallback {

        void reschedule();

        void onDataFetcherReady(Key sourceKay, Object data, DataFetcher<?> fetcher,
                                DataSource dataSource, Key attemptedKey);

        void onDataFetcherFailed(Key attemptedKey, Exception e, DataFetcher<?> fetcher,
                                 DataSource dataSource);
    }

    /**
     * Attempts to a single new {@link DataFetcher}.
     *
     * @return  True if a {@link DataFetcher} was started, and false otherwise.
     */
    boolean startNext();

    /**
     * Attempts to cancel the currently running fetcher.
     */
    void cancel();
}
