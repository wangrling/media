package com.android.mm.libgdx.android;

/**
 * Class defining the configuration of an {@link AndroidApplication}. Allows you to disable the use of
 * the accelerometer to save battery among other things.
 */
public class AndroidApplicationConfiguration {
    /** number of bits per color channel */
    public int r = 5, g = 6, b = 5, a = 0;

    /** number of bits for depth and stencil buffer */
    public int depth = 16, stencil = 0;


    public ResolutionStrategy resolutionStrategy = new FillResolutionStrategy();
}
