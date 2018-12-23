package com.android.mm.libgdx.gdx;


import com.android.mm.libgdx.gdx.files.FileHandle;

public interface Files {

    /**
     * Indicates how to resolve a path to a file.
     */
    public enum FileType {
        /**
         * Path relative to the root of the classpath. Classpath files are always readonly.
         * Note that classpath files are not compatible with some functionality on Android,
         * such as {@link Audio#newSound(FileHandle)} and
         * {@link Audio#newMusic(FileHandle)}.
         * 从包名开始计算，具体到本例中就是com.android.mm的文件路径。
         */
        Classpath,

        // Android都有具体的函数实现。

        /**
         * Path relative to the asset directory on Android.
         * Internal files are always readonly.
         */
        Internal,

        /**
         * Path relative to the root of te SD card on Android.
         * 目前大多数设备已经没有SD卡，但是默认外围存储。
         */
        External,

        /** Path that is a fully qualified, absolute filesystem path. To ensure portability across platforms use absolute files only
         * when absolutely (heh) necessary. */
        Absolute,

        /**
         * Path relative to the private files directory on Android.
         */
        Local;
    }

    /**
     * Return a handle representing a file or directory.
     *
     * @param path  type determines how the path is resolved.
     * @param type  Determines how the path is resolved.
     * @return
     * @throws RuntimeException if the type is classpath or internal and the the file
     * does not exist.
     */
    public FileHandle getFileHandle(String path, FileType type);

    public FileHandle classPath(String path);

    public FileHandle internal(String path);

    public FileHandle external(String path);

    public FileHandle absolute(String path);

    public FileHandle local(String path);

    /**
     * Returns the external storage path directory. This is the SD card on Android.
     */
    public String getExternalStoragePath();

    /**
     * Returns true if the external storage is ready for file IO. Eg, on Android,
     * the SD card is not available when mounted for use with a PC.
     */
    public boolean isExternalStorageAvailable();

    /**
     * Returns the local storage path directory. This is the private files directory
     * on Android.
     */
    public String getLocalStoragePath();

    /**
     * Returns true if the local storage is ready for file I/O.
     */
    public boolean isLocalStorageAvailable();
}
