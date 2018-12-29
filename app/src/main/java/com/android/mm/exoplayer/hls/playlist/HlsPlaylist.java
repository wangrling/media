package com.android.mm.exoplayer.hls.playlist;

import com.google.android.exoplayer2.offline.FilterableManifest;

import java.util.Collections;
import java.util.List;

/** Represents an HLS playlist. */

public abstract class HlsPlaylist implements FilterableManifest<HlsPlaylist> {

    /**
     * The base uri. Used to resolve relative paths.
     */
    public final String baseUri;

    /**
     * The list of tags in the playlist.
     */
    public final List<String> tags;

    /**
     * Whether the media is formed of independent segments, as defined by the
     * #EXT-X-INDEPENDENT-SEGMENTS tags.
     */
    public final boolean hasIndependentSegments;

    protected HlsPlaylist(String baseUri, List<String> tags, boolean hasIndependentSegments) {
        this.baseUri = baseUri;
        this.tags = Collections.unmodifiableList(tags);
        this.hasIndependentSegments = hasIndependentSegments;
    }
}
