package com.google.android.camera.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.camera.debug.Log;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class SettingsManager {

    private static final Log.Tag TAG = new Log.Tag("SettingsManager");

    private final Object mLock;
    private final Context mContext;
    private final String mPackageName;
    private final SharedPreferences mDefaultPreferences;
    private SharedPreferences mCustomPreferences;
    private final DefaultStore mDefaultStore = new DefaultStore();

    public static final String MODULE_SCOPE_PREFIX = "_preferences_module_";
    public static final String CAMERA_SCOPE_PREFIX = "_preferences_camera_";

    /**
     * A List of OnSettingChangedListener's, maintained to compare to new
     * listeners and prevent duplicate registering.
     */
    private final List<OnSettingChangedListener> mListeners =
            new ArrayList<OnSettingChangedListener>();

    /**
     * A List of OnSharedPreferenceChangeListener's, maintained to hold pointers
     * to actually registered listeners, so they can be unregistered.
     */
    private final List<SharedPreferences.OnSharedPreferenceChangeListener> mSharedPreferenceListeners =
            new ArrayList<>();

    public SettingsManager(Context context) {

    }
}
