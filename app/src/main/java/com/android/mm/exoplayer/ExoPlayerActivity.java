package com.android.mm.exoplayer;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.android.mm.R;

import androidx.annotation.Nullable;

// 加入10万行library/core/的代码，然后模仿测试集学习。
public class ExoPlayerActivity extends Activity {

    WebView mExoPlayerViewView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_exo);
        setActionBar(findViewById(R.id.toolbar));

        mExoPlayerViewView = findViewById(R.id.exoPlayerWebView);
        mExoPlayerViewView.loadUrl("file:///android_asset/html/exoplayer.html");
    }
}
