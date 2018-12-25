package com.android.mm.grafika.generated;

import android.opengl.GLES30;
import android.util.Log;

import com.android.mm.grafika.GrafikaActivity;

import java.io.File;
import java.io.IOException;

/**
 * Generates a very simple movie.  The screen is divided into eight rectangles, and one
 * rectangle is highlighted in each frame.
 * <p>
 * To add a little flavor, the timing of the frames speeds up as the movie continues.
 */

public class MovieEightRects extends GenerateMovie {

    private final String TAG = GrafikaActivity.TAG;

    private static final String MIME_TYPE = "video/avc";
    private static final int WIDTH = 320;
    private static final int HEIGHT = 240;
    private static final int BIT_RATE = 2000000;
    private static final int NUM_FRAMES = 32;
    private static final int FRAMES_PER_SECOND = 30;

    // RGB color values for generated frames
    private static final int TEST_R0 = 0;
    private static final int TEST_G0 = 136;
    private static final int TEST_B0 = 0;
    private static final int TEST_R1 = 236;
    private static final int TEST_G1 = 50;
    private static final int TEST_B1 = 186;

    @Override
    public void create(File outputFile, ContentManager.ProgressUpdater prog) {
        if (mMovieReady) {
            throw new RuntimeException("Already created");
        }

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

        Log.d(TAG, "MovieEightRects complete: " + outputFile);
        mMovieReady = true;
    }

    /**
     * Generates a frame of data using GL commands.  We have an 8-frame animation
     * sequence that wraps around.  It looks like this:
     * <pre>
     *   0 1 2 3
     *   7 6 5 4
     * </pre>
     * We draw one of the eight rectangles and leave the rest set to the clear color.
     */
    private void generateFrame(int frameIndex) {
        frameIndex %= 8;

        int startX, startY;
        if (frameIndex < 4) {
            // (0,0) is bottom-left in GL
            startX = frameIndex * (WIDTH / 4);
            startY = HEIGHT / 2;
        } else {
            startX = (7 - frameIndex) * (WIDTH / 4);
            startY = 0;
        }


        GLES30.glClearColor(TEST_R0 / 255.0f, TEST_G0 / 255.0f, TEST_B0 / 255.0f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // 裁剪矩形区域
        GLES30.glEnable(GLES30.GL_SCISSOR_TEST);
        GLES30.glScissor(startX, startY, WIDTH / 4, HEIGHT / 2);
        GLES30.glClearColor(TEST_R1 / 255.0f, TEST_G1 / 255.0f, TEST_B1 / 255.0f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glDisable(GLES30.GL_SCISSOR_TEST);
    }

    /**
     * Generates the presentation time for the frame N, in nanoseconds.
     *
     * First 8 frames at 8 fps, next 8 at 16 fps, rest at 30 fps.
     */
    private static long computePresentationTimeNsec(int frameIndex) {
        final long ONE_BILLION = 1000000000;
        long time;
        if (frameIndex < 8) {
            // 8 fps
            return frameIndex * ONE_BILLION / 8;
        } else {
            time = ONE_BILLION;
            frameIndex -= 8;
        }
        if (frameIndex < 8) {
            return time + frameIndex * ONE_BILLION / 16;
        } else {
            time += ONE_BILLION / 2;
            frameIndex -= 8;
        }
        return time + frameIndex * ONE_BILLION / 30;
    }
}