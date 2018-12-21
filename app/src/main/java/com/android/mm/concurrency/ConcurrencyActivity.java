package com.android.mm.concurrency;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.android.mm.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ConcurrencyActivity extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_concurrency);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mWebView = findViewById(R.id.concurrencyWebView);
        mWebView.loadUrl("file:///android_asset/html/concurrency_java.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.concurrency_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.java: {
                mWebView.loadUrl("file:///android_asset/html/concurrency_java.html");
                break;
            }
            case R.id.cpp: {
                mWebView.loadUrl("file:///android_asset/html/concurrency_cpp.html");
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
