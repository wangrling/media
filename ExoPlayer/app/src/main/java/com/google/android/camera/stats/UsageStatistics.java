package com.google.android.camera.stats;

import android.content.Context;
import android.graphics.Rect;

import com.google.android.camera.exif.ExifInterface;
import com.google.android.camera.ui.TouchCoordinate;

import java.util.HashMap;
import java.util.List;

public class UsageStatistics {

    public static final long VIEW_TIMEOUT_MILLIS = 0;
    public static final int NONE = -1;

    private static UsageStatistics sInstance;

    public static UsageStatistics instance() {
        if (sInstance == null) {
            sInstance = new UsageStatistics();
        }
        return sInstance;
    }

    public void initialize(Context context) {

    }

    public void mediaInteraction(String ref, int interactionType, int cause, float age) {

    }

    public void mediaView(String ref, long modifiesdMillis, float zoom) {

    }

    public void foreground(int source, int mode, boolean isKeyguardLocked,
                           boolean isKeyguardSecure, boolean startupOnCreate, long controlTime) {

    }

    public void background() {

    }

    public void cameraFrameDrop(double delta, double previousDeltaMs) {

    }

    public void jankDetactionEnabled() {

    }

    public void storageWarning(long storageSpace) {

    }

    public void videoCaptureDoneEvent(String ref, long durationMsec, boolean front,
                                      float zoom, int width, int height, long size,
                                      String flashSetting, boolean gridLineOn) {

    }

    public void photoCaptureDoneEvent(int mode, String fileRef, ExifInterface exifRef,
                                      boolean front, boolean isHDR, float zoom,
                                      String flashSetting, boolean gridLineOn,
                                      Float timeSeconds, Float processingTime,
                                      TouchCoordinate touch, Boolean volumeButtonShutter,
                                      List<Camera2FaceProxy> faces, Float lensDistance,
                                      Rect activeSensorSize) {

    }

    public void cameraFailure(int cause, String info, int agentAction, int agentState) {

    }

    public void changeScreen(int newScreen, Integer interactionCause) {

    }

    public void controlUsed(int control) {

    }

    public void tapToFocus(TouchCoordinate touch, Float duration) {

    }

    public void reportMemoryConsumed(HashMap memoryData, String reportType) {

    }

}
