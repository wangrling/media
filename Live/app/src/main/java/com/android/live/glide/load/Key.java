package com.android.live.glide.load;

import java.nio.charset.Charset;
import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * An interface that uniquely identifies some put of data.
 */

public interface Key {

    String STRING_CHARSET_NAME = "UTF-8";
    Charset CHARSET = Charset.forName(STRING_CHARSET_NAME);

    /**
     * Adds all uniquely identifying information to the given digest.
     *
     * <p>Note - Using {@link MessageDigest#reset()} inside of this method will result in
     * undefined behavior.
     */
    void updateDiskCacheKey(@NonNull MessageDigest messageDigest);


    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
