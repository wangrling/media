package com.android.live.camera.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.live.camera.stats.profiler.Profile;
import com.android.live.camera.stats.profiler.Profiler;
import com.android.live.camera.stats.profiler.Profilers;

public class FirstRunDetector {
    private static final long UNKNOWN = -1;
    // Default SharedPreferences key for when the application was first used.
    private static final String CLIENT_FIRST_USE_TIME = "client_first_use_time_millis";

    private static class Singleton {
        private static final FirstRunDetector INSTANCE = new FirstRunDetector(
                Profilers.instance().guard());
    }

    public static FirstRunDetector instance() {
        return Singleton.INSTANCE;
    }

    private final Profile mProfile;
    private long mTimeOfFirstRun;
    /** Flag set to true if and only if first run of application is detected. */
    private boolean mIsFirstRun = false;

    private FirstRunDetector(Profiler profiler) {
        mProfile = profiler.create("FirstRunDetector getTimeOfFirstRun");
    }

    /**
     * Return true if this is the first time the app was opened.
     */
    public boolean isFirstRun() {
        return mIsFirstRun;
    }

    /**
     * Return true if this is the first time the app was opened.
     */
    public long getTimeOfFirstRun() {
        return mTimeOfFirstRun;
    }

    /**
     * Clear the first run flag.
     */
    public void clear() {
        mIsFirstRun = false;
    }

    /**
     * Returns time the app was first used, or UNKNOWN if the
     * client is too old. This could be moved a separate utility class.
     *
     * @param context Application context.
     */
    public void initializeTimeOfFirstRun(Context context) {
        mProfile.start();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mProfile.mark("PreferenceManager.getDefaultSharedPreferences");

        // Read time of first installation.
        long timeOfFirstUseMillis = preferences.getLong(CLIENT_FIRST_USE_TIME, 0);
        mProfile.mark("preferences.getLong");

        // Write installation time if not set.
        if (timeOfFirstUseMillis == 0) {
            SharedPreferences cameraPrefs = context.getSharedPreferences(
                    context.getPackageName() + "_preferences_camera", Context.MODE_PRIVATE);
            mProfile.mark("getSharedPreferences");

            // If we can find previous Shared Preferences, this is not a new install.
            boolean isUpgrade = cameraPrefs.getAll().size() > 0 || preferences.getAll().size() > 0;

            // Preference CLIENT_FIRST_USE_TIME is set to UNKNOWN for preference
            // upgrades and the actual first use time for new installs.  We call
            // System.currentTimeMillis() to match the log timebase.
            timeOfFirstUseMillis = isUpgrade ? UNKNOWN : System.currentTimeMillis();
            preferences.edit().putLong(CLIENT_FIRST_USE_TIME, timeOfFirstUseMillis).apply();
            mProfile.mark("preferences.edit()");

            if (!isUpgrade) {
                mIsFirstRun = true;
            }
        }
        mTimeOfFirstRun = timeOfFirstUseMillis;
        mProfile.stop();
    }
}
