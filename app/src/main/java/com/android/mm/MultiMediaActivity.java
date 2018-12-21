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

    private static List<String> mDataSet = Arrays.asList(
            "Grafika", "ExoPlayer", "Algorithms", "Music", "Ndk", "Concurrency",
            "Oboe", "Rtmp", "Amr", "MediaCts", "Arch", "Patterns", "Camera",
            "Filament", "Codec", "Sonic"
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

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, mDataSet));
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
