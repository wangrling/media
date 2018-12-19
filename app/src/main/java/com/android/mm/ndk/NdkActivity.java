package com.android.mm.ndk;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class NdkActivity extends ListActivity {
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    private static final String[][] TESTS = {
            {
                "GLES3JNI",
                    "Demonstrates how to use OpenGL ES 3.0 from JNI/native code."
            },
            {
                "Bitmap Plasma",
                    "Render a plasma (等离子体) effect in an Android."
            },
            {
                "JNI Callback (难)",
                    "C程序使用(*env)->结构，C++程序使用(env)->结构。" +
                            "Demonstrate calling back to Java from C code."
            },
            {
                "OpenSLES Audio",
                    "Plays and records sounds with the C++ OpenSLES API using JNI."
            },
            {
                "Native Codec",
                    "Uses the Native Media Codec API to play a video."
            }

    };

    private Class CLASSES[] = {
            GLES3JNIActivity.class,
            PlasmaActivity.class,
            JNICallbackActivity.class,
            NativeAudioActivity.class,
            NativeCodecActivity.class
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new SimpleAdapter(this, createList(),
                android.R.layout.two_line_list_item, new String[] {TITLE, DESCRIPTION},
                new int[] {android.R.id.text1, android.R.id.text2}));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(this, CLASSES[position]));
    }

    private List<Map<String, String>> createList() {
        List<Map<String, String>> testList = new ArrayList<>();

        for (String[] test : TESTS) {
            Map<String, String> tmp = new HashMap<>();
            tmp.put(TITLE, test[0]);
            tmp.put(DESCRIPTION, test[1]);

            testList.add(tmp);
        }
        return testList;
    }
}
