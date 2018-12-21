package com.android.mm.patterns;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;

public class PatternsActivity extends Activity {
    public static final String TAG = "Patterns";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(getApplicationContext());
        webView.loadUrl("file:///android_asset/html/design_patterns.html");
        setContentView(webView);
    }
}
