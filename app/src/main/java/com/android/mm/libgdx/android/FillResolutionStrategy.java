package com.android.mm.libgdx.android;

import android.view.View;

/**
 * This {@link ResolutionStrategy} will stretch the GLSurfaceView to full screen. FillResolutionStrategy
 * is the default {@link ResolutionStrategy} if none is specified.
 */
public class FillResolutionStrategy implements ResolutionStrategy {

    @Override
    public MeasureDimension calcMeasures(int widthMeasureSpec, int heightMeasureSpec) {

        // Extracts the size from the supplied measure specification.
        final int width = View.MeasureSpec.getSize(widthMeasureSpec);
        final int height = View.MeasureSpec.getSize(heightMeasureSpec);

        return new MeasureDimension(width, height);
    }
}
