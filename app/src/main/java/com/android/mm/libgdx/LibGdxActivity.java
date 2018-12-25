package com.android.mm.libgdx;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 后期需要增加Input输入事件系统的测试。
 * 增加2D/3D显示系统。
 */

public class LibGdxActivity extends ListActivity {

    private static List<String> TESTS = Arrays.asList(
            "WindowTest", "AudioTest"
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TESTS));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        try {
            Class c = Class.forName(TESTS.get(position) + "Activity");
            startActivity(new Intent(this, c));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
