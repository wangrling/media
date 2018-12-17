package com.android.mm.grafika;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * Grafika, a dumping ground for Android graphics & media hacks.
 */
public class GrafikaActivity extends ListActivity {
    public static final String TAG = "Grafika";

    // map keys
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CLASS_NAME = "class_name";

    /**
     * Each entry has three strings: the test title, the test description, and the name of
     * the activity class.
     */
    private static final String[][] TESTS = {
            {
                "{util} OpenGL ES info",
                    "Dumps info about graphics drivers",
                    "GlesInfoActivity"},
            { "{util} Color bars",
                    "Shows RGB color bars",
                    "ColorBarActivity" },
            { "Simple Canvas in TextureView",
                    "Renders with Canvas as quickly as possible",
                    "TextureViewCanvasActivity" },
            { "Simple GL in TextureView",
                    "Renders with GL as quickly as possible",
                    "TextureViewGLActivity" },
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new SimpleAdapter(this, createActivityList(),
                android.R.layout.two_line_list_item, new String[] {TITLE, DESCRIPTION},
                new int[] {android.R.id.text1, android.R.id.text2}));
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)listView.getItemAtPosition(position);
        Intent intent = (Intent) map.get(CLASS_NAME);
        startActivity(intent);
    }

    /**
     * Creates the list of activities from the string arrays.
     */
    private List<Map<String, Object>> createActivityList() {
        List<Map<String, Object>> testList = new ArrayList<>();

        for (String[] test : TESTS) {
            // 需要理解Map结构体
            Map<String, Object> tmp = new HashMap<>();
            tmp.put(TITLE, test[0]);
            tmp.put(DESCRIPTION, test[1]);
            Intent intent = new Intent();
            // Do the class name resolution here, so we crash up front rather than when the
            // activity list item is selected if the class name is wrong.
                try {
                    Class cls = Class.forName(getPackageName() + ".grafika." + test[2]);
                    intent.setClass(this, cls);
                    tmp.put(CLASS_NAME, intent);
                } catch (ClassNotFoundException cnfe) {
                    throw new RuntimeException("Unable to find " + test[2], cnfe);
                }
            testList.add(tmp);
        }

        // 不需要排序
        // Collections.sort(testList, TEST_LIST_COMPARATOR);

        return testList;
    }
}
