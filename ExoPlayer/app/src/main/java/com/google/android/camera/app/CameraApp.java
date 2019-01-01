package com.google.android.camera.app;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.google.android.camera.stats.UsageStatistics;
import com.google.android.camera.stats.profiler.Profile;
import com.google.android.camera.stats.profiler.Profilers;
import com.google.android.camera.util.AndroidContext;
import com.google.android.camera.util.AndroidServices;

/**
 * The camera application class containing important services and functionality
 * to be used across modules.
 */
public class CameraApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Android context must be the first item initialized.
        Context context = getApplicationContext();
        AndroidContext.initialize(context);

        // This will measure and write to the exception handler if the time
        // between any two calls or the total time from start to stop is over 10ms.
        // 测量函数的执行时间
        // 需要清除后台app才能开始打印。
        Profile guard = Profilers.instance().guard("CameraApp onCreate()");

        // It is important that this gets calls early in execution before the
        // app has had the opportunity to touch shared preferences.
        FirstRunDetector.instance().initializeTimeOfFirstRun(context);
        guard.mark("initializeTimeOfFirstRun");

        UsageStatistics.instance().initialize(this);
        guard.mark("UsageStatistics.initialize");

        clearNotifications();
        guard.stop("clearNotifications");
    }

    /**
     * Clears all notifications. This cleans up notifications that we might have
     * created earlier but remained after a crash.
     */
    private void clearNotifications() {
        NotificationManager manager = AndroidServices.instance().provideNotificationManager();
        if (manager != null) {
            manager.cancelAll();
        }
    }
}
