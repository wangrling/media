package com.android.mm.libgdx.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.mm.libgdx.gdx.ApplicationListener;

public class AndroidApplication extends Activity implements AndroidApplicationBase {

    protected AndroidGraphics graphics;
    protected AndroidAudio audio;

    public Handler handler;

    protected ApplicationListener listener;

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

    /** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
     * input, render via OpenGL and so on. Uses a default {@link AndroidApplicationConfiguration}.
     * <p>
     * Note: you have to add the returned view to your layout!
     *
     * @param listener the {@link ApplicationListener} implementing the program logic
     * @return the GLSurfaceView of the application */
    public View initializeForView(ApplicationListener listener) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        return initializeForView(listener, config);
    }

    /** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
     * input, render via OpenGL and so on. You can configure other aspects of the application with the rest of the fields in the
     * {@link AndroidApplicationConfiguration} instance.
     * <p>
     * Note: you have to add the returned view to your layout!
     *
     * @param listener the {@link ApplicationListener} implementing the program logic
     * @param config the {@link AndroidApplicationConfiguration}, defining various settings of the application (use accelerometer,
     *           etc.).
     * @return the GLSurfaceView of the application */
    public View initializeForView(ApplicationListener listener, AndroidApplicationConfiguration config) {
        init(listener, config, true);
        // 返回的View应该是GLSurfaceView的子类，这样调用render函数渲染。
        return graphics.getView();

    }

    private void init(ApplicationListener listener, AndroidApplicationConfiguration config, boolean isForView) {

        graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ?
                new FillResolutionStrategy() : config.resolutionStrategy);

        audio = new AndroidAudio(this, config);

        this.listener = listener;

        // Add a specialized audio lifecycle listener.


    }


}
