package com.android.live;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.live.camera.CameraActivity;
import com.android.live.codec.OpenMaxALActivity;
import com.android.live.filament.jni.Platform;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = (item) -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getApplicationContext().startActivity(
                            new Intent(getApplicationContext(), OpenMaxALActivity.class));
                    return true;
                case R.id.player:
                    return true;
                case R.id.camera:
                    getApplicationContext().startActivity(
                            new Intent(getApplicationContext(), CameraActivity.class));
                            // new Intent(getApplicationContext(), MockActivity.class));
                    return true;
            }
            return false;
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 测试使用

        // MainActivity: platform is android true
        Log.d(TAG, "platform is android " + Platform.isAndroid());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
