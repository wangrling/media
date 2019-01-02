package com.google.android.camera.util;

import android.util.Log;

/**
 * This class tracks the timing of important state changes in camera app (e.g latency
 * of cold/warm start of the activity, mode switch duration, etc). We can then query
 * these values from the instrument tests, which will be helpful for tracking camera
 * app performance and regression tests.
 * 记录冷启动，暖启动和模式切换时间。
 */

public class CameraPerformanceTracker {

    // Event types to track.
    public static final int ACTIVITY_START = 0;
    public static final int ACTIVITY_PAUSE = 1;
    public static final int ACTIVITY_RESUME = 2;
    public static final int MODE_SWITCH_START = 3;
    public static final int FIRST_PREVIEW_FRAME = 5;
    public static final int UNSET = -1;

    private static final String TAG = "CameraPerformanceTracker";
    private static final boolean DEBUG = false;
    private static CameraPerformanceTracker sInstance;

    // Internal tracking time.
    private long mAppStartTime = UNSET;
    private long mAppResumeTime = UNSET;
    private long mModeSwitchStartTime = UNSET;

    // Duration and/or latency or later querying.
    private long mFirstPreviewFrameLatencyColdStart = UNSET;
    private long mFirstPreviewFrameLatencyWarmStart = UNSET;
    // TODO: Need to how to best track the duration for each switch from/to pair.
    private long mModeSwitchDuration = UNSET;

    private CameraPerformanceTracker() {
        // Private constructor to ensure that it can only be created from within
        // the class.
    }

    /**
     * This gets called when an important state change happens. Based on the type
     * of the event/state change, either we will record the time of the event, or
     * calculate the duration/latency.
     *
     * @param eventType type of a event to track
     */
    public static void onEvent(int eventType) {
        if (sInstance == null) {
            sInstance = new CameraPerformanceTracker();
        }
        long currentTime = System.currentTimeMillis();
        switch (eventType) {
            case ACTIVITY_START:
                sInstance.mAppStartTime = currentTime;
                break;
            case ACTIVITY_PAUSE:
                sInstance.mFirstPreviewFrameLatencyWarmStart = UNSET;
                break;
            case ACTIVITY_RESUME:
                sInstance.mAppResumeTime = currentTime;
            case FIRST_PREVIEW_FRAME:
                Log.d(TAG, "First preview frame received");
                if (sInstance.mFirstPreviewFrameLatencyColdStart == UNSET) {
                    // 第一次启动
                    // Code start.
                    sInstance.mFirstPreviewFrameLatencyColdStart = currentTime -
                            sInstance.mAppStartTime;
                } else {
                    // Warm start.
                    sInstance.mFirstPreviewFrameLatencyWarmStart =
                            currentTime - sInstance.mAppResumeTime;
                }
                // If the new frame is triggered by the mode switch, track the duration.
                if (sInstance.mModeSwitchStartTime != UNSET) {
                    sInstance.mModeSwitchDuration = currentTime - sInstance.mModeSwitchStartTime;
                    sInstance.mModeSwitchStartTime = UNSET;
                }
                break;
            case MODE_SWITCH_START:
                sInstance.mModeSwitchStartTime = currentTime;
                break;
            default:
                break;
        }
        if (DEBUG && eventType == FIRST_PREVIEW_FRAME) {
            Log.d(TAG, "Mode switch duration: " + (sInstance.mModeSwitchDuration
                    == UNSET ? "UNSET" : sInstance.mModeSwitchDuration));
            Log.d(TAG, "Cold start latency: " + (sInstance.mFirstPreviewFrameLatencyColdStart
                    == UNSET ? "UNSET" : sInstance.mFirstPreviewFrameLatencyColdStart));
            Log.d(TAG, "Warm start latency: " + (sInstance.mFirstPreviewFrameLatencyWarmStart
                    == UNSET ? "UNSET" : sInstance.mFirstPreviewFrameLatencyWarmStart));
        }
    }

    //TODO: Hook up these getters in the instrument tests.
    /**
     * Gets the latency of a cold start of the app, measured from the time onCreate
     * gets called to the time first preview frame gets received.
     *
     * @return latency of a cold start. If no instances have been created, return
     *         UNSET.
     */
    // 从activity onCreate到显示第一帧的时间。
    public static long getColdStartLatency() {
        if (sInstance == null) {
            return UNSET;
        }
        return sInstance.mFirstPreviewFrameLatencyColdStart;
    }

    /**
     * 从start到第二帧的时间。
     * Gets the latency of a warm start of the app, measured from the time onResume gets
     * called to the time next preview frame gets received.
     *
     * @return latency of a warm start. If no instance have been created, return UNSET.
     */
    public static long getWarmStartLantency() {
        if (sInstance == null) {
            return UNSET;
        }

        return sInstance.mFirstPreviewFrameLatencyWarmStart;
    }

    /**
     * 模式切换时间
     * Gets the duration of the mode switch, measured from the start of a mode switch
     * to the time new preview frame gets received.
     * @return
     */
    public static long getModeSwitchDuration() {
        if (sInstance == null) {
            return UNSET;
        }
        return sInstance.mModeSwitchDuration;
    }
}
