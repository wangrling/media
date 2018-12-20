package com.android.mm.mediacts;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

// 这个主要是Android源码中的多媒体Cts测试部分。
// 核心的三个文件AudioTrack.java, SurfaceView.java, SurfaceTexture.java
// 分别代表音视频的渲染。

public class MediaCtsActivity extends ListActivity {

    private static final String[] CTS_TESTS = {
        "AudioTrack: "
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CTS_TESTS));
    }
}
