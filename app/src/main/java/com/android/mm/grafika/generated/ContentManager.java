package com.android.mm.grafika.generated;

import android.content.Context;

import com.android.mm.grafika.GrafikaActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Manages content generated by the app.
 * <p>
 * [ Originally this was going to prepare stuff on demand, but it's easier to just
 * create it all up front on first launch. ]
 * <p>
 * Class is thread-safe.
 */

public class ContentManager {
    private static final String TAG = GrafikaActivity.TAG;

    // Enumerated content tags.  These are used as indices into the mContent ArrayList,
    // so don't make them sparse.
    // TODO: consider using String tags and a HashMap?  prepare() is currently fragile,
    //       depending on the movies being added in tag-order.  Could also just use a plain array.
    public static final int MOVIE_EIGHT_RECTS = 0;
    public static final int MOVIE_SLIDERS = 1;

    private static final int[] ALL_TAGS = new int[] {
            MOVIE_EIGHT_RECTS,
            MOVIE_SLIDERS
    };

    // Housekeeping.
    private static final Object sLock = new Object();
    private static ContentManager sInstance = null;

    private boolean mInitialized = false;
    private File mFilesDir;
    private ArrayList<Content> mContent;

    /**
     * Returns the singleton instance.
     */
    public static ContentManager getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new ContentManager();
            }
            return sInstance;
        }
    }

    private ContentManager() {}

    public static void initialize(Context context) {
        ContentManager manager = getInstance();
        synchronized (sLock) {
            if (!manager.mInitialized) {
                manager.mFilesDir = context.getFilesDir();
                manager.mContent = new ArrayList<>();
                manager.mInitialized = true;
            }
        }
    }


    public interface ProgressUpdater {
        /**
         * Updates a progress meter.
         * @param percent   Percent completed (0-100).
         */
        void updateProgress(int percent);
    }
}
