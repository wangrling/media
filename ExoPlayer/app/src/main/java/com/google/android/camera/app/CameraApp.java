package com.google.android.camera.app;

import android.app.Application;
import android.content.Context;

import com.google.android.camera.util.AndroidContext;

/**
 * The camera application class containing important services and functionality
 * to be used across modules.
 */
public class CameraApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Android context must be the first item initialized.
        Context context = getApplicationContext();
        AndroidContext.initialize(context);
    }


}
