package com.google.android.libgdx.android;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import com.google.android.libgdx.ApplicationListener;
import com.google.android.libgdx.ApplicationLogger;
import com.google.android.libgdx.Audio;
import com.google.android.libgdx.Files;
import com.google.android.libgdx.Graphics;
import com.google.android.libgdx.LifecycleListener;
import com.google.android.libgdx.Net;
import com.google.android.libgdx.utils.Array;
import com.google.android.libgdx.utils.Clipboard;
import com.google.android.libgdx.utils.SnapshotArray;

import java.util.prefs.Preferences;

public class AndroidApplication extends Activity implements AndroidApplicationBase {

    protected AndroidGraphics graphics;

    protected AndroidInput input;


    public View initializeForView(ApplicationListener listener) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        return initializeForView(listener, config);
    }

    public View initializeForView(ApplicationListener listener, AndroidApplicationConfiguration config) {
        init(listener, config, true);

        return graphics.getView();
    }

    private void init(ApplicationListener listener, AndroidApplicationConfiguration config, boolean isForView) {

        graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ?
                new FillResolutionStrategy() : config.resolutionStrategy);
    }


    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public Array<Runnable> getRunnables() {
        return null;
    }

    @Override
    public Array<Runnable> getExecutedRunnables() {
        return null;
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return null;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Audio getAudio() {
        return null;
    }

    @Override
    public AndroidInput getInput() {
        return null;
    }

    @Override
    public Files getFiles() {
        return null;
    }

    @Override
    public Net getNet() {
        return null;
    }

    @Override
    public void log(String tag, String message) {

    }

    @Override
    public void log(String tag, String message, Throwable exception) {

    }

    @Override
    public void error(String tag, String message) {

    }

    @Override
    public void error(String tag, String message, Throwable exception) {

    }

    @Override
    public void debug(String tag, String message) {

    }

    @Override
    public void debug(String tag, String message, Throwable exception) {

    }

    @Override
    public void setLogLevel(int logLevel) {

    }

    @Override
    public int getLogLevel() {
        return 0;
    }

    @Override
    public void setApplicationLogger(ApplicationLogger applicationLogger) {

    }

    @Override
    public ApplicationLogger getApplicationLogger() {
        return null;
    }

    @Override
    public ApplicationType getType() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public long getJavaHeap() {
        return 0;
    }

    @Override
    public long getNativeHeap() {
        return 0;
    }

    @Override
    public Preferences getPreferences(String name) {
        return null;
    }

    @Override
    public Clipboard getClipboard() {
        return null;
    }

    @Override
    public void postRunnable(Runnable runnable) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public SnapshotArray<LifecycleListener> getLifecycleListeners() {
        return null;
    }

    @Override
    public Window getApplicationWindow() {
        return null;
    }

    @Override
    public void useImmersiveMode(boolean b) {

    }

    @Override
    public Handler getHandler() {
        return null;
    }
}
