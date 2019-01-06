package com.android.live.glide.load.engine;

import com.android.live.glide.load.DataSource;
import com.android.live.glide.load.Key;
import com.android.live.glide.load.data.DataFetcher;

/**
 * Generate a series of {@link DataFetcher} using registered {@link ModelLoader} and a model
 */

public interface DataFetcherGenerator {

    /**
     * Called when the generator has finished loading data from a {@link DataFetcher}.
     */
    interface FetchReadyCallback {

        /**
         * Request that we call startNext() again on a Glide owned thread.
         */
        void reschedule();

        /**
         * Notifies the callback that the load is complete.
         *
         * @param sourceKay     The id of the the loaded data.
         * @param data      The loaded data, or null if the load failed.
         * @param fetcher   The data fetcher we attempted to load from.
         * @param dataSource    The data source we were loading from.
         * @param attemptedKey  The key we were loading data from (may be an alternate).
         */
        void onDataFetcherReady(Key sourceKay, Object data, DataFetcher<?> fetcher,
                                DataSource dataSource, Key attemptedKey);

        /**
         * Notifies the callback when the load fails.
         *
         * @param attemptedKey  The key we were using to load (may be an alternate).
         * @param e     The exception that caused the load to fail.
         * @param fetcher   The fetcher we were loading from.
         * @param dataSource    The data source we were loading from.
         */
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
