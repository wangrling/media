package com.android.mm.grafika;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.mm.R;
import com.android.mm.grafika.generated.ContentManager;
import com.android.mm.grafika.utils.AboutBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * Grafika, a dumping ground for Android graphics & media hacks.
 */
public class GrafikaActivity extends Activity {
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
                    "Dumps info about graphics drivers, create a 1x1 pbuffer.",
                    "GlesInfoActivity"},
            { "{util} Color bars",
                    "Shows RGB color bars, implements SurfaceHolder.Callback interface.",
                    "ColorBarActivity" },
            { "Simple Canvas in TextureView",
                    "Renders with Canvas as quickly as possible, set a dirty rect.",
                    "TextureViewCanvasActivity" },
            { "Simple GL in TextureView",
                    "Renders with GLES in " +
                            "a `TextureView`, rather than a `GLSurfaceView`.",
                    "TextureViewGLActivity" },
            {
                "Play video (SurfaceView)",
                    "Plays the video track from an MP4 file in SurfaceView.",
                    "PlayMovieSurfaceActivity"
            },
            {
                "Play video (TextureView)",
                    "Uses a `TextureView` for output. You can use the\n" +
                            "  checkboxes to loop playback and/or play the frames at a fixed rate of 60 FPS.",
                    "PlayTextureActivity"
            },

            {
                "Continuous capture",
                    "Currently hard-wired to try to capture 7 seconds of video from the camera at " +
                            "6MB/sec, preferrably 15fps 720p.",
                    "ContinuousCaptureActivity"
            },

            {
                "Double decode",
                    "Plays the two auto-generated videos.  Note they play at different rates.",
                    "DoubleDecodeActivity"
            },

            {
                "Hardware scaler",
                    "Shows GL rendering with on-the-fly surface size changes.",
                "HardwareScalerActivity"
            }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grafika);

        // One-time singleton initialization; requires activity context to get file location.
        ContentManager.initialize(this);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new SimpleAdapter(this, createActivityList(),
                R.layout.wrapper_two_line_list_item, new String[] {TITLE, DESCRIPTION},
                new int[] {android.R.id.text1, android.R.id.text2}));

        listView.setOnItemClickListener((parent, view, position, id) -> {
                    Map<String, Object> map = (Map<String, Object>) listView.getItemAtPosition(position);
                    Intent intent = (Intent) map.get(CLASS_NAME);
                    startActivity(intent);
                });

        // 生成MovieSliders.mp4和MovieEightRects.mp4两个视频文件。
        ContentManager cm = ContentManager.getInstance();
        if (!cm.isContentCreated(this)) {
            ContentManager.getInstance().createAll(this);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                AboutBox.display(this);
                return true;
            case R.id.link:
                ContentManager.getInstance().createAll(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
