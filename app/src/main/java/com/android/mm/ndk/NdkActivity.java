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
            }
    };

    private Class CLASSES[] = {
            GLES3JNIActivity.class
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
