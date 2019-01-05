package com.android.live.filament

import android.app.Activity
import com.android.live.filament.jni.Filament

class HelloTriangleActivity : Activity() {

    // Make sure to initialize Filament first
    // This loads the JNI library needed by most API calls.
    companion object {
        init {
            Filament.init();
        }
    }
}