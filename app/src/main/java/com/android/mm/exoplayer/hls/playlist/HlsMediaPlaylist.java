package com.android.mm.exoplayer.hls.playlist;

import com.google.android.exoplayer2.offline.StreamKey;

import java.util.List;

/** Represents an HLS playlist. */

public final class HlsMediaPlaylist extends HlsPlaylist {

    protected HlsMediaPlaylist(String baseUri, List<String> tags, boolean hasIndependentSegments) {
        super(baseUri, tags, hasIndependentSegments);
    }

    @Override
    public HlsPlaylist copy(List<StreamKey> streamKeys) {
        return null;
    }
}
