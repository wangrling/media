package com.android.live.filament.jni;

public abstract class Platform {

    private static Platform mCurrentPlatform = null;

    public static boolean isAndroid() {
        return "The Android Project".equalsIgnoreCase(System.getProperty("java.vendor"));
    }

    static Platform get() {
        if (mCurrentPlatform == null) {
            // noinspection EmptyCatchBlock
            try {
                if (isAndroid()) {
                    Class<?> clazz = Class.forName("com.android.live.filament.jni.AndroidPlatform");
                    mCurrentPlatform = (Platform) clazz.newInstance();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            if (mCurrentPlatform == null) {

            }
        }
        return mCurrentPlatform;
    }

    Platform() {}
}
