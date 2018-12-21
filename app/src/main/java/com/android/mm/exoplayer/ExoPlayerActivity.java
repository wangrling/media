package com.android.mm.exoplayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.android.mm.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

// 加入10万行library/core/的代码，然后模仿测试集学习。
public class ExoPlayerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_exo);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(true);
            // 只能用AppCompat显示图标。
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new WebViewFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    /**
     * Handles app bar item clicks.
     * @param item  item clicked.
     * @return      True if one of the defined items was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.link: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, new LinkFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            }
            case R.id.info: {


                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
