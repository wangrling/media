package com.android.live.camera.stats;

import android.graphics.Rect;
import android.hardware.camera2.params.Face;

/**
 * Wraps the Camera2 required class to insulate Kit-Kat devices from Camera2 API
 * contamination.
 */
public class Camera2FaceProxy {
    private final Rect mFaceRect;
    private final int mScore;

    public Camera2FaceProxy(Rect faceRect, int score) {
        mFaceRect = faceRect;
        mScore = score;
    }

    public static Camera2FaceProxy from(Face face) {
        Camera2FaceProxy convertedFace = new Camera2FaceProxy(face.getBounds(), face.getScore());
        return convertedFace;
    }

    public Rect getFaceRect() {
        return mFaceRect;
    }

    public int getScore() {
        return mScore;
    }
}
