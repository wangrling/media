package com.android.mm.music;

import android.Manifest;
import android.os.Bundle;

import com.android.mm.music.custom.PermissionActivity;

import androidx.annotation.Nullable;

public class MusicActivity extends MediaPlaybackActivity {

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (PermissionActivity.checkAndRequestPermission(this, REQUIRED_PERMISSIONS)) {

        }

        super.onCreate(savedInstanceState);


    }
}
