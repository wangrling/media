package com.android.live.glide.load.engine;

import androidx.annotation.NonNull;

/**
 * A resource interface that wraps a particular type so that it can be pooled and reused.
 *
 * @param <Z>   The type of resource wrapped by this class.
 */

public interface Resource<Z> {

    /**
     * @return  The {@link Class} of the wrapped resource.
     */
    @NonNull
    Class<Z> getResourceClass();


}
