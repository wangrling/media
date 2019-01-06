package com.android.live.camera;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.location.Location;

import com.android.live.camera.app.MediaSaver;
import com.android.live.camera.debug.Log;
import com.android.live.camera.exif.ExifInterface;

public class MediaSaverImpl implements MediaSaver {

    private static final Log.Tag TAG = new Log.Tag("MediaSaverImpl");
    private static final String VIDEO_BASE_URI = "content://media/external/video/media";

    /** The memory limit for unsaved image is 30MB. */
    // TODO: Revert this back to 20 MB when CaptureSession API supports saving
    // bursts.
    private static final int SAVE_TASK_MEMORY_LIMIT = 30 * 1024 * 1024;

    private final ContentResolver mContentResolver;


    /** Memory used by the total queued save request, in bytes. */
    private long mMemoryUse;



    public MediaSaverImpl(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
        mMemoryUse = 0;
    }

    @Override
    public boolean isQueueFull() {
        return false;
    }

    @Override
    public void addImage(byte[] data, String title, long date, Location loc, int width, int height, int orientation, ExifInterface exif, OnMediaSavedListener l) {

    }

    @Override
    public void addImage(byte[] data, String title, long date, Location loc, int width, int height, int orientation, ExifInterface exif, OnMediaSavedListener l, String mimeType) {

    }

    @Override
    public void addImage(byte[] data, String title, long date, Location loc, int orientation, ExifInterface exif, OnMediaSavedListener l) {

    }

    @Override
    public void addImage(byte[] data, String title, Location loc, int width, int height, int orientation, ExifInterface exif, OnMediaSavedListener l) {

    }

    @Override
    public void addVideo(String path, ContentValues values, OnMediaSavedListener l) {

    }

    @Override
    public void setQueueListener(QueueListener l) {

    }
}
