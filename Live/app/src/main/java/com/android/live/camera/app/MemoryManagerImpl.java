package com.android.live.camera.app;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import com.android.live.camera.debug.Log;
import com.android.live.camera.util.AndroidServices;
import com.android.live.camera.util.GservicesHelper;

import java.util.HashMap;

// 内存管理

public class MemoryManagerImpl implements MemoryManager, MediaSaver.QueueListener, ComponentCallbacks2 {

    private static final Log.Tag TAG = new Log.Tag("MemoryManagerImpl");

    /**
     * Let's signal only 70% of max memory is allowed to be used by native code
     * to allow a buffer for special captures.
     */
    private static final float MAX_MEM_ALLOWED = 0.70f;

    private static final int[] sCriticalStates = new int[] {
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL
    };

    /**
     * The maximum amount of memory allowed to be allocated in native code (in
     * megabytes)
     */
    private final int mMaxAllowedNativeMemory;

    /**
     * Used to query a breakdown of current memory consumption and memory
     * thresholds.
     */
    private final MemoryQuery mMemoryQuery;

    public static MemoryManagerImpl create(Context context, MediaSaver mediaSaver) {
        ActivityManager activityManager = AndroidServices.instance().provideActivityManager();
        int maxAllowedNativeMemory = getMaxAllowedNativeMemory(context);
        MemoryQuery mMemoryQuery = new MemoryQuery(activityManager);
        MemoryManagerImpl memoryManager = new MemoryManagerImpl(maxAllowedNativeMemory, mMemoryQuery);
        mediaSaver.setQueueListener(memoryManager);

        return memoryManager;
    }

    /**
     * Use {@link #create(Context, MediaSaver)} to make sure it's wired up
     * correctly.
     */
    private MemoryManagerImpl(int maxAllowedNativeMemory, MemoryQuery memoryQuery) {
        mMaxAllowedNativeMemory = maxAllowedNativeMemory;
        mMemoryQuery = memoryQuery;
        Log.d(TAG, "Max native memory: " + mMaxAllowedNativeMemory + " MB");

    }

    private static int getMaxAllowedNativeMemory(Context context) {
        // First check whether we have a system override.
        int maxAllowedOverrideMb = GservicesHelper.getMaxAllowedNativeMemoryMb(context
                .getContentResolver());
        if (maxAllowedOverrideMb > 0) {
            Log.d(TAG, "Max native memory overridden: " + maxAllowedOverrideMb);
            return maxAllowedOverrideMb;
        }

        ActivityManager activityManager = AndroidServices.instance().provideActivityManager();

        // Use the max of the regular memory class and the large memory class.
        // This is defined as the maximum memory allowed to be used by the
        // Dalvik heap, but it's safe to assume the app can use the same amount
        // once more in native code.
        return (int) (Math.max(activityManager.getMemoryClass(),
                activityManager.getLargeMemoryClass()) * MAX_MEM_ALLOWED);
    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onQueueStatus(boolean full) {

    }

    @Override
    public void addListener(MemoryListener listener) {

    }

    @Override
    public void removeListener(MemoryListener listener) {

    }

    @Override
    public int getMaxAllowedNativeMemoryAllocation() {
        return 0;
    }

    @Override
    public HashMap queryMemory() {
        return null;
    }
}
