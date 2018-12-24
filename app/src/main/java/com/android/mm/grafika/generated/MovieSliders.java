package com.android.mm.grafika.generated;

import android.appwidget.AppWidgetHost;
import android.opengl.GLES30;
import android.util.Log;

import com.android.mm.grafika.GrafikaActivity;

import java.io.File;
import java.io.IOException;

/**
 * Generates a simple movie, featuring two small rectangles that slide across the screen.
 */

public class MovieSliders extends GenerateMovie {

    private static final String TAG = GrafikaActivity.TAG;

    private static final String MIME_TYPE = "video/avc";
    private static final int WIDTH = 480;       // note 480x640, not 640x480
    private static final int HEIGHT = 640;
    private static final int BIT_RATE = 5000000;
    private static final int FRAMES_PER_SECOND = 30;

    /**
     * Generates a frame of data using GL commands.
     */
    private void generateFrame(int frameIndex) {
        final int BOX_SIZE = 80;
        frameIndex %= 240;
        int xpos, ypos;

        int absIndex = Math.abs(frameIndex - 120);
        xpos = absIndex * WIDTH / 120;
        ypos = absIndex * HEIGHT / 120;

        float lumaf = absIndex / 120.0f;

        GLES30.glClearColor(lumaf, lumaf, lumaf, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glEnable(GLES30.GL_SCISSOR_TEST);
        GLES30.glScissor(BOX_SIZE / 2, ypos, BOX_SIZE, BOX_SIZE);
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glScissor(xpos, BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
        GLES30.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glDisable(GLES30.GL_SCISSOR_TEST);
    }

    /**
     * Generates the presentation time for frame N, in nanoseconds.  Fixed frame rate.
     */
    private static long computePresentationTimeNsec(int frameIndex) {
        final long ONE_BILLION = 1000000000;
        return frameIndex * ONE_BILLION / FRAMES_PER_SECOND;
    }

    @Override
    public void create(File outputFile, ContentManager.ProgressUpdater prog) {
        if (mMovieReady) {
            throw new RuntimeException("Already created");
        }

        final int NUM_FRAMES = 120;

        try {
            prepareEncoder(MIME_TYPE, WIDTH, HEIGHT, BIT_RATE, FRAMES_PER_SECOND, outputFile);

            for (int i = 0; i < NUM_FRAMES; i++) {
                // Drain any data from the encoder into the muxer.
                drainEncoder(false);

                // Generate a frame and submit it.
                generateFrame(i);
                submitFrame(computePresentationTimeNsec(i));

                prog.updateProgress(i * 100 / NUM_FRAMES);
            }

            // Send end-of-stream and drain remaining output.
            drainEncoder(true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            releaseEncoder();
        }

        Log.d(TAG, "MovieSliders complete: " + outputFile);
        mMovieReady = true;
    }
}
