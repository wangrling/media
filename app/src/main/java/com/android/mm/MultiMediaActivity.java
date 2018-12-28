package com.android.mm;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Show a list using a RecyclerView.
 */

public class MultiMediaActivity extends ListActivity {

    // 半年计划，不再增加新主题。
	// 全力写代码！
    // 应该先抄完再修改。
    // 每天目标，七千行代码。
    // 从上往下，完成为止，不要挑剔。
    // 不要轻易修改源代码，能运行就好，改出BUG还要自己修复。
    private static List<String> mDataSet = Arrays.asList(
            // 主要是多媒体的介绍。
            /**
             * 
             */
            "Grafika",
            // 多媒体框架。
            "ExoPlayer",
            // 算法知识积累。
            "Algorithms",
            // 高通本地播放器。
            "Music",
            // 谷歌android-ndk示例。
            "Ndk",
            // 并发相关知识和计算机基础。
            "Concurrency",
            // 低延迟音频库。
            "Oboe",
            // 游戏引擎。
            "LibGdx",
            // 网络流协议。
            "Rtmp",
            // 音频协议。
            "Amr",
            // 源码多媒体测试集。
            "MediaCts",
            // 编程框架。
            "Arch",
            // 常用的设计模式。
            "Patterns",
            // 系统相机。
            "Camera",
            // 实时渲染库。
            "Filament",
            // 编解码库
            "Codec",
            // 音效处理。
            "Sonic",
            // 网页多媒体。
            "WebRtc",
            // 系统图片预览。
            "Gallery",
            // 系统闹钟程序。
            "DeskClock",
            // 系统计算器。
            "Calculator",
            // 系统录音程序。
            "SoundRecorder",
            // 系统多媒体管理。
            "MediaProvider",
            // 系统新功能库。
            "Sunflower",
            // 物理模拟库。
            "Bullet",
            // 多媒体底层库。
            "FFmpeg",
            // 图形加载和显示库。
            "Fresco",
            // 三维显示。
            "OpenGL",
            // 视频编辑器。
            "VideoEditor",
            // 材料设计。
            "Plaid",
            // 音视频播放器。
            "VLC",
            // HLS协议。
            "HLS",
            // OpenMAX协议。
            "OpenMAX",
            // Png和WebP图片编解码
            "PP",
            // H264协议。
            "H264"

    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_media_multi);
        setActionBar(findViewById(R.id.toolbar));

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setListAdapter(new ArrayAdapter<>(this,
                R.layout.wrapper_simple_expandable_list_item, mDataSet));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.link: {
                break;
            }
            case R.id.info: {
                break;
            }
            case R.id.settings: {
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            Class cls = Class.forName(getPackageName() + "." + mDataSet.get(position).toLowerCase() +
                    "." + mDataSet.get(position) + "Activity");
            startActivity(new Intent(this, cls));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
