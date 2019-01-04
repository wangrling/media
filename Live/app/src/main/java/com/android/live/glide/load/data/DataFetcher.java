package com.android.live.glide.load.data;

import com.android.live.glide.Priority;
import com.android.live.glide.load.DataSource;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Lazily retrieves data that can be used to load a resource.
 *
 */

public interface DataFetcher<T> {

    interface DataCallback<T> {

        /**
         * @param data  The loaded data if the load succeeded, or with {@code null} if
         *              the load failed.
         */
        void onDataReady(@Nullable T data);

        /**
         * Called when the load fails.
         *
         * @param e A non-null {@link Exception} indicating why the load failed.
         */
        void onLoadFailed(@Nullable Exception e);
    }

    /**
     * Fetch data from which a resource can be decoded.
     *
     * @param priority
     * @param callback
     */
    void loadData(@Nonnull Priority priority, @NonNull DataCallback<? super T> callback);

    void cleanup();

    void cancel();

    /**
     * @return  The class of the data this fetcher will attempt to obtain.
     */
    @NonNull
    Class<T> getDataClass();


    DataSource getDataSource();
}
