package com.android.mm.exoplayer.demo;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.android.mm.R;
import com.android.mm.exoplayer.ui.PlayerView;
import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import androidx.annotation.Nullable;

// 后期将使用expresso自动化测试。

public class SimplePlayerActivity extends Activity {

    private PlayerView playerView;
    private PlayerManager player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_simple);
        playerView = findViewById(R.id.simplePlayerView);
        player = new PlayerManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        player.init(this, playerView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        player.reset();
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }

    final class PlayerManager implements AdsMediaSource.MediaSourceFactory {

        private final DataSource.Factory dataSourceFactory;

        private SimpleExoPlayer player;
        private long contentPosition;

        public PlayerManager(Context context) {
            dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.getString(R.string.app_name)));
        }

        public void init(Context context, PlayerView playerView) {
            // Create a default track selector.
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create a player instance.
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            // Bind the player to the view.
            playerView.setPlayer(player);

            // This is the MediaSource representing the content media (i.e. not the ad).
            MediaSource contentMediaSource = buildMediaSource(
                    Uri.parse("file:///android_asset/mpeg_4_avc_aac_24fps.mp4"));

            // Prepare the player with the source.
            player.seekTo(contentPosition);
            player.prepare(contentMediaSource);
            player.setPlayWhenReady(true);
        }

        public void reset() {
            if (player != null) {

                contentPosition = player.getContentPosition();
                player.release();
                player = null;
            }
        }

        public void release() {
            if (player != null) {
                player.release();
                player = null;
            }
        }


        @Override
        public MediaSource createMediaSource(Uri uri) {
            return buildMediaSource(uri);
        }

        // Internal methods
        private MediaSource buildMediaSource(Uri uri) {
            @C.ContentType int type = Util.inferContentType(uri);
            switch (type) {
                case C.TYPE_DASH:
                    // return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                case C.TYPE_HLS:
                    // return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                case C.TYPE_OTHER:
                    return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }
        }

        @Override
        public int[] getSupportedTypes() {
            // IMA does not support Smooth Streaming ads.
            return new int[] {C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
        }
    }

    /*
    private Uri mp4VideoUri;

    SimpleExoPlayer player;
    PlayerView playerView;
    MediaSource videoSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_simple);

        playerView = findViewById(R.id.simplePlayerView);

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
*/
}
