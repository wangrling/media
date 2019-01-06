package com.android.live.filament.samples

import android.app.Activity
import android.view.Choreographer
import android.view.SurfaceView
import com.android.live.filament.android.Engine
import com.android.live.filament.android.Filament
import com.android.live.filament.android.UiHelper

class HelloTriangleActivity : Activity() {

    // Make sure to initialize Filament first
    // This loads the JNI library needed by most API calls.
    companion object {
        init {
            Filament.init();
        }
    }

    // The View we want to render into
    private lateinit var surfaceView: SurfaceView
    // UiHelper is provided by Filament to manage SurfaceView and SurfaceTexture
    private lateinit var uiHelper: UiHelper
    // Choreographer is used to schedule new frames
    private lateinit var choreographer: Choreographer

    // Engine creates and destroys Filament resources
    // Each engine must be accessed from a single thread of your choosing
    // Resources cannot be shared across engines
    private lateinit var engine: Engine

}