package com.android.live.filament.android;

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
                    Class<?> clazz = Class.forName("com.android.live.filament.android.AndroidPlatform");
                    mCurrentPlatform = (Platform) clazz.newInstance();
                }
            } catch (Exception e) {

            }
            if (mCurrentPlatform == null) {
                mCurrentPlatform = new UnknownPlatform();
            }
        }
        return mCurrentPlatform;
    }

    Platform() {}

    abstract void log(String message);
    abstract void warn(String message);

    abstract boolean validateStreamSource(Object object);
    abstract boolean validateSurface(Object object);
    abstract boolean validateSharedContext(Object object);
    abstract long getSharedContextNativeHandle(Object sharedContext);

    private static class UnknownPlatform extends Platform {

        @Override
        void log(String message) {
            System.out.println(message);
        }

        @Override
        void warn(String message) {
            System.out.println(message);
        }

        @Override
        boolean validateStreamSource(Object object) {
            return false;
        }

        @Override
        boolean validateSurface(Object object) {
            return false;
        }

        @Override
        boolean validateSharedContext(Object object) {
            return false;
        }

        @Override
        long getSharedContextNativeHandle(Object sharedContext) {
            return 0;
        }
    }
}
