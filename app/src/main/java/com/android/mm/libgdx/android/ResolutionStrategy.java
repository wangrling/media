package com.android.mm.libgdx.android;

/**
 * Will manipulate the GLSurfaceView. Gravity is always center. The width and height of the view
 * will be determined by the classes implementing {@link ResolutionStrategy}.
 */
public interface ResolutionStrategy {

    public MeasureDimension calcMeasures (final int widthMeasureSpec, final int heightMeasureSpec);

    public static class MeasureDimension {

        public final int width;
        public final int height;

        public MeasureDimension(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
