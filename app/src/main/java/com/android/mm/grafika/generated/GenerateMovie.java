package com.android.mm.grafika.generated;

import android.media.MediaCodec;
import android.media.MediaMuxer;

import com.android.mm.grafika.GrafikaActivity;
import com.android.mm.grafika.gles.EglCore;
import com.android.mm.grafika.gles.WindowSurface;

import java.io.File;

/**
 * Base class for generated movies.
 */

public abstract class GenerateMovie implements Content {

    private static final String TAG = GrafikaActivity.TAG;
    private static final boolean VERBOSE = false;

    // set by sub-class to indicate that the movie has been generated
    // TODO: remove this now?
    protected boolean mMovieReady = false;

    // "live" state during recording
    private MediaCodec.BufferInfo mBufferInfo;
    private MediaCodec mEncoder;
    private MediaMuxer mMuxer;
    private EglCore mEglCore;
    private WindowSurface mInputSurface;
    private int mTrackIndex;
    private boolean mMuxerStarted;

    /**
     * Creates the movie content. Usually called from an async task thread.
     */
    public abstract void create(File outputFile, ContentManager.ProgressUpdater prog);


}
