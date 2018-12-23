package com.android.mm.libgdx.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.android.mm.libgdx.gdx.ApplicationListener;

public class AndroidApplication extends Activity implements AndroidApplicationBase {

    protected AndroidGraphics graphics;
    protected AndroidAudio audio;

    public Handler handler;

    /**
     * This method has to be called int the {@link Activity#onCreate(Bundle)} method. It set up all
     * the things necessary to get input, render via OpenGL and so on.
     * Uses a default {@link AndroidApplicationConfiguration}.
     *
     * @param listener the {@link ApplicationListener} implementing the program logic.
     */
    public void initialize(ApplicationListener listener) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(listener, config);
    }

    /**
     * This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the
     * things necessary to get input, render via OpenGL and so on. You can configure other aspects of
     * the application with the rest of the fields in the {@link AndroidApplicationConfiguration} instance.
     *
     * @param listener  the {@link ApplicationListener} implementing the program logic
     * @param config    the {@link AndroidApplicationConfiguration}, defining various settings of the
     *                  application (use accelerometer, etc).
     */
    private void initialize(ApplicationListener listener, AndroidApplicationConfiguration config) {
        init(listener, config, false);
    }

    private void init(ApplicationListener listener, AndroidApplicationConfiguration config, boolean isForView) {

        graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ?
                new FillResolutionStrategy() : config.resolutionStrategy);
    }


}
