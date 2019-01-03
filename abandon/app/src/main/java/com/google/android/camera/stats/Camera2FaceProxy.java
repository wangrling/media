package com.google.android.camera.stats;

import android.graphics.Rect;
import android.hardware.camera2.params.Face;

/**
 * Wraps the Camera2 required class to insulate (使隔离) Kit-Kat devices
 * from Camera2 API contamination (玷污).
 */

public class Camera2FaceProxy {

    private final Rect mFactRect;
    private final int mScore;

    public Camera2FaceProxy(Rect faceRect, int score) {
        mFactRect = faceRect;
        mScore = score;
    }

    public static Camera2FaceProxy from(Face face) {
        Camera2FaceProxy convertedFace = new Camera2FaceProxy(face.getBounds(), face.getScore());
        return convertedFace;
    }

    public Rect getFactRect() {
        return mFactRect;
    }

    public int getScore() {
        return mScore;
    }
}
