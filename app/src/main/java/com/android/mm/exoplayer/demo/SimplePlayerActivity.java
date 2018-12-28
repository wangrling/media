package com.android.mm.exoplayer.demo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.android.mm.R;
import com.android.mm.exoplayer.ui.PlayerView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import androidx.annotation.Nullable;

// 后期将使用expresso自动化测试。

public class SimplePlayerActivity extends Activity {

    private Uri mp4VideoUri;

    SimpleExoPlayer player;
    PlayerView playerView;
    MediaSource videoSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_simple);

        player = ExoPlayerFactory.newSimpleInstance(this);

        playerView.setPlayer(player);

        mp4VideoUri = Uri.parse("file:///android_asset/mpeg_4_avc_aac_24fps.mp4");

        // Produces DataSource instance through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "MM"));

        videoSource = new ExtractorMediaSource
                .Factory(dataSourceFactory)
                .createMediaSource(mp4VideoUri);

        Player.EventListener eventListener = new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                }
            }
        };

        player.addListener(eventListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        player.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }
}
