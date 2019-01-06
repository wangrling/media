package com.android.live.glide.load;

import com.android.live.glide.load.engine.Resource;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

/**
 * An interface for writing data from a resource to some persistent data store (i.e. a local File
 * cache).
 *
 * @param <T> The type of the data contained by the resource.
 */

public interface ResourceEncoder<T> extends Encoder<Resource<T>> {

    // specializing the generic arguments
    @Nonnull
    EncodeStrategy getEncodeStrategy(@NonNull Options options);
}
