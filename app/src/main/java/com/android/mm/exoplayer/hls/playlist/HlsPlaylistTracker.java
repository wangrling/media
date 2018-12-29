package com.android.mm.exoplayer.hls.playlist;

public class HlsPlaylistTracker {

    /** Listener for primary playlist changes. */
    public interface PrimaryPlaylistListener {

        /**
         * Called when the primary playlist changes.
         *
         * @param mediaPlaylist The primary playlist new snapshot.
         */
        void onPrimaryPlaylistRefreshed(HlsMediaPlaylist mediaPlaylist);
    }
}
