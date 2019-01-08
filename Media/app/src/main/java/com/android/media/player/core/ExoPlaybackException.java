package com.android.media.player.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public final class ExoPlaybackException extends Exception {

    /**
     * The type of source that produced the error. One of {@link #TYPE_SOURCE}, {@link #TYPE_RENDERER}
     * or {@link #TYPE_UNEXPECTED}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_SOURCE, TYPE_RENDERER, TYPE_UNEXPECTED})
    public @interface Type {}

    /**
     * The error occurred loading data from a {@link MediaSource}.
     * <p>
     * Call {@link #getSourceException()} to retrieve the underlying cause.
     */
    public static final int TYPE_SOURCE = 0;

    /**
     * The error occurred in a {@link Renderer}.
     * <p>
     * Call {@link #getRendererException()} to retrieve the underlying cause.
     */
    public static final int TYPE_RENDERER = 1;

    /**
     * The error was an unexpected {@link RuntimeException}.
     * <p>
     * Call {@link #getUnexpectedException()} to retrieve the underlying cause.
     */
    public static final int TYPE_UNEXPECTED = 2;

    /**
     * The type of the playback failure. One of {@link #TYPE_SOURCE}, {@link #TYPE_RENDERER} and
     * {@link #TYPE_UNEXPECTED}.
     */
    @Type public final int type;

    /**
     * If {@link #type} is {@link #TYPE_RENDERER}, this is the index of the renderer.
     */
    public final int rendererIndex;

    private final Throwable cause;

    private ExoPlaybackException(@Type int type, Throwable cause, int rendererIndex) {
        super(cause);
        this.type = type;
        this.cause = cause;
        this.rendererIndex = rendererIndex;
    }
}
