package com.android.mm.libgdx.gdx.files;

import com.android.mm.libgdx.gdx.Files;

import java.io.File;

/**
 * Represents a file or directory on the filesystem, classpath, Android SD card, or
 * Android assets directory. FileHandles are create via {@link Files} instance.
 *
 * Because some of the file types are backed by composite files and may be compressed
 * (for example, if they are in an Android .apk or are found via the classpath), the
 * methods for extracting a {@link #path} or {@link #file} may not be appropriate for
 * all types. Use the Reader or Stream methods here to hide these dependencies from
 * your platform independent code.
 */
public class FileHandle {
    protected File file;
    // 文件类型是指它的存储位置。
    protected Files.FileType type;

    protected FileHandle() {

    }
}
