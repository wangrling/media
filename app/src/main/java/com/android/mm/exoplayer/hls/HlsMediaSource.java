package com.android.mm.exoplayer.hls;

import com.android.mm.exoplayer.hls.playlist.HlsMediaPlaylist;
import com.android.mm.exoplayer.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;

import androidx.annotation.Nullable;

public final class HlsMediaSource extends BaseMediaSource implements
        HlsPlaylistTracker.PrimaryPlaylistListener {


    @Override
    protected void prepareSourceInternal(ExoPlayer player, boolean isTopLevelSource, @Nullable TransferListener mediaTransferListener) {

    }

    @Override
    protected void releaseSourceInternal() {

    }

    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {

    }

    @Override
    public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator) {
        return null;
    }

    @Override
    public void releasePeriod(MediaPeriod mediaPeriod) {

    }

    @Override
    public void onPrimaryPlaylistRefreshed(HlsMediaPlaylist mediaPlaylist) {

    }
}
