package com.google.android;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ExoPlayerActivity extends ListActivity {

    private static final String[] CASES = {
            "Timeline由Window和Period组成，可以将Timeline理解为时间轴，Period理解为每个媒体文件的播放周期，Window" +
                    "包含一个或者多个Period用于播放。Timeline适应的场景很复杂，可以先简单理解一个本地媒体文件和" +
                    "一个Window一一对应，后续再理解其它几种和流媒体相关的定义。"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, CASES));
    }
}
