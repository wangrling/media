package com.android.mm.exoplayer.ui.spherical;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import com.android.mm.exoplayer.ui.PlayerView;

import javax.annotation.Nullable;

public class SphericalSurfaceView extends GLSurfaceView {

    private @Nullable SurfaceListener surfaceListener;


    public SphericalSurfaceView(Context context) {
        super(context);
    }

    /**
     * Sets the {@link SurfaceListener} used to listen to surface events.
     *
     * @param listener The listener for surface events.
     */
    public void setSurfaceListener(@Nullable SurfaceListener listener) {
        surfaceListener = listener;
    }


    /** Sets the {@link SingleTapListener} used to listen to single tap events on this view. */
    public void setSingleTapListener(@Nullable SingleTapListener listener) {
        // touchTracker.setSingleTapListener(listener);
    }

    public void setVideoComponent(Object o) {

    }

    /**
     * This listener can be used to be notified when the {@link Surface} associated with this view is
     * changed.
     */
    public interface SurfaceListener {

        /**
         * Invoked when the surface is changed or there isn't one anymore. Any previous surface
         * shouldn't be used after this call.
         *
         * @param surface The new surface or null if there isn't one anymore.
         */
        void surfaceChanged(@Nullable Surface surface);
    }
}
