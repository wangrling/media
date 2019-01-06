package com.android.live.glide.load;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * An interface for writing data to some persistent data store (i.e. a local File cache).
 *
 * @param <T>   The type of the data that will be written.
 */

interface Encoder<T> {

    /**
     * Writes the given data to the given output stream and returns True if the write completed
     * successfully and should be committed.
     *
     * @param data The data to write.
     * @param file The File to write the data to.
     * @param options The put of options to apply when encoding.
     */
    boolean encode(@NonNull T data, @NonNull File file, @NonNull Options options);
}
