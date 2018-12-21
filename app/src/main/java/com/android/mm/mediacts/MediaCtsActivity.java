package com.android.mm.mediacts;

import android.app.Activity;
import android.app.ListActivity;
import android.media.MediaExtractor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

// 这个主要是Android源码中的多媒体Cts测试部分。
// 核心的三个文件AudioTrack.java, SurfaceView.java, SurfaceTexture.java
// 分别代表音视频的渲染。

public class MediaCtsActivity extends ListActivity {

    private static final String[] CTS_TESTS = {
            "The AudioTrack class manages and plays a single audio resource for Java applications. " +
                "It allows streaming of PCM audio buffers to the audio sink for playback. This is " +
                "achieved by \"pushing\" the data to the AudioTrack object using one of the " +
                "{@link #write(byte[], int, int)}, {@link #write(short[], int, int)}, " +
                "and {@link #write(float[], int, int, int)} methods.",

            "The SurfaceView provides a dedicated drawing surface embedded inside of a view hierarchy. " +
                    "The Surface will be created for you while the SurfaceView's window is " +
                    "visible; you should implement {@link SurfaceHolder.Callback#surfaceCreated} " +
                    "and {@link SurfaceHolder.Callback#surfaceDestroyed} to discover when the " +
                    "Surface is created and destroyed as the window is shown and hidden.",

            "A TextureView can be used to display a content stream. Unlike {@link SurfaceView}, " +
                    "TextureView does not create a separate window but behaves as a regular View. " +
                    "This key difference allows a TextureView to be moved, transformed, animated, etc." +
                    "Using a TextureView is simple: all you need to do is get its {@link SurfaceTexture}.",

            "MediaExtractor facilitates extraction of demuxed, typically encoded,  " +
                    "media data from a data source.",

            "MediaCodec class can be used to access low-level media codec, i.e. encoder/decoder components."




    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CTS_TESTS));
    }
}
