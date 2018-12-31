package com.google.android.core.source.ads;

import android.net.Uri;

import com.google.android.core.C;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public final class AdPlaybackState {

    /**
     * Represents a group of ads, with information about their states.
     * 表示一组广告以及它们的状态。
     */
    public static final class AdGroup {
        /** The number of ads in the ad group, or {@link C#LENGTH_UNSET} is unknown. */
        public final int count;

        /** The URI of each ad in the ad group. */
        public final Uri[] uris;

        /** The state of each ad in the ad group. */
        public final @AdState int[] states;

        /** The durations of each ad in the ad group, in microseconds. */
        public final long[] durationsUs;

        /** Creates a new ad group with an unspecified number of ads. */
        public AdGroup() {
            this(
                    /* count= */ C.LENGTH_UNSET,
                    /* states= */ new int[0],
                    /* uris= */ new Uri[0],
                    /* durationsUs= */ new long[0]);
        }

        private AdGroup(int count, @AdState int[] states, Uri[] uris, long[] durationsUs) {
            this.count = count;
            this.states = states;
            this.uris = uris;
            this.durationsUs = durationsUs;
        }
    }

    /**
     * Represents the state of an ad in an ad group. One of {@link #AD_STATE_UNAVAILABLE}, {@link
     * #AD_STATE_AVAILABLE}, {@link #AD_STATE_SKIPPED}, {@link #AD_STATE_PLAYED} or {@link
     * #AD_STATE_ERROR}.
     * SOURCE表示Annotations are to be discarded by the compiler.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            AD_STATE_UNAVAILABLE,
            AD_STATE_AVAILABLE,
            AD_STATE_SKIPPED,
            AD_STATE_PLAYED,
            AD_STATE_ERROR,
    })
    public @interface AdState {}
    /** State for an ad that does not yet have a URL. */
    public static final int AD_STATE_UNAVAILABLE = 0;
    /** State for an ad that has a URL but has not yet been played. */
    public static final int AD_STATE_AVAILABLE = 1;
    /** State for an ad that was skipped. */
    public static final int AD_STATE_SKIPPED = 2;
    /** State for an ad that was played in full. */
    public static final int AD_STATE_PLAYED = 3;
    /** State for an ad that could not be loaded. */
    public static final int AD_STATE_ERROR = 4;

    /** Ad playback state with no ads. */
    public static final AdPlaybackState NONE = new AdPlaybackState();
}
