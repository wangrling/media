package com.android.mm.ndk;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.mm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class NdkActivity extends ListActivity {

    public static final String TAG = "NDK";

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    private static final String[][] TESTS = {
            {
                "GL ES3 JNI",
                    "Demonstrates how to use OpenGL ES 3.0 from JNI/native code."
            },
            {
                "Bitmap Plasma",
                    "Render a plasma (等离子体) effect in an Android."
            },
            {
                // JavaVM* 指针怎么获取到的？
                "JNI Callback",
                    "C程序使用(*env)->结构，C++程序使用(env)->结构。" +
                            "Demonstrate calling back to Java from C code."
            },
            {
                "Native Audio",
                    "Plays and records sounds with the C++ OpenSLES API using JNI, not in fast audio path."
            },
            {
                "Native Codec",
                    "Uses the Native Media Codec API to play a video."
            },
            {
                "OppenMAX AL",
                    "Uses OpenMAX AL to play a video."
            },
            {
                "Fast Audio Path",
                    "Uses OpenSL ES to create a player and recorder in Android Fast Audio Path."
            },
            {
                "Teapots",
                    "Demonstrates multiple frame rate throttoling techniques."
            },
            {
                "Camera",
                    "Preview camera images with AReadImage and take jpeg photos."
            },
            {
                "Endless Tunnel",
                    "Implement a game using Android native glue."
            }

    };

    private Class CLASSES[] = {
            GLES3JNIActivity.class,
            PlasmaActivity.class,
            JNICallbackActivity.class,
            NativeAudioActivity.class,
            NativeCodecActivity.class,
            OpenMaxActivity.class,
            FastAudioActivity.class,
            TeapotActivity.class
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new SimpleAdapter(this, createList(),
                R.layout.wrapper_two_line_list_item, new String[] {TITLE, DESCRIPTION},
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
