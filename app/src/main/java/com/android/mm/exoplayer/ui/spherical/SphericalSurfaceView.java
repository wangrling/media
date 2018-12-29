package com.android.mm.exoplayer.ui.spherical;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import javax.annotation.Nullable;

public class SphericalSurfaceView extends GLSurfaceView {


    public SphericalSurfaceView(Context context) {
        super(context);
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
        void surfaceChange(@Nullable Surface surface);
    }
}
