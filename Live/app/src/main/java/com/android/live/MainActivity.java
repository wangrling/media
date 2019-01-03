package com.android.live;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.android.live.camera.CameraActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = (item) -> {
            switch (item.getItemId()) {
                case R.id.home:
                    return true;
                case R.id.player:
                    return true;
                case R.id.camera:
                    getApplicationContext().startActivity(
                            new Intent(getApplicationContext(), CameraActivity.class));
                    return true;
            }
            return false;
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
