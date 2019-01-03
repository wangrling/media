package com.google.android.camera.ui;

/**
 * Touch coordinate.
 */
public class TouchCoordinate {

    private float x;
    private float y;
    private float maxX;
    private float maxY;

    /**
     * Constructor
     *
     * @param x     X value for the touch, with 0 typically the lowest value.
     * @param y     Y value for the touch, with 0 typically the lowset value.
     * @param maxX  Highest X value possible for any touch.
     * @param maxY  Highest Y value possible for any touch.
     */
    public TouchCoordinate(float x, float y, float maxX, float maxY) {
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }
}
