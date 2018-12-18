package com.android.mm.music.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.android.mm.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PermissionActivity extends Activity {
    private static final String PREVIOUS_INTENT = "previous_intent";
    private static final String KEY_FROM_PREVIEW = "from_preview";
    private static final String REQUEST_PERMISSIONS = "request_permissions";
    private static final int REQUEST_CODE = 100;
    private static final String PACKAGE_URL_SCHEME = "package:";

    private Intent mPreviewIntent;
    private boolean mIsFromPreview = false;
    private String[] mRequestedPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreviewIntent = (Intent) getIntent().getExtras().get(PREVIOUS_INTENT);
        mIsFromPreview = getIntent().getBooleanExtra(KEY_FROM_PREVIEW, false);

        mRequestedPermissions = getIntent().getStringArrayExtra(REQUEST_PERMISSIONS);
        if (savedInstanceState == null && mRequestedPermissions != null) {
            String[] neededPermissions =
                    checkRequestedPermission(this, mRequestedPermissions);
            if (neededPermissions != null && neededPermissions.length != 0) {
                requestPermissions(neededPermissions, REQUEST_CODE);
            }
        }
    }

    public static String[] checkRequestedPermission(Activity activity, String[] permissionName) {
        boolean isPermissionGranted = true;
        List<String> needRequestPermission = new ArrayList<String>();
        for (String tmp : permissionName) {
            isPermissionGranted = (PackageManager.PERMISSION_GRANTED ==
                    activity.checkSelfPermission(tmp));
            if (!isPermissionGranted) {
                needRequestPermission.add(tmp);
            }
        }
        String[] needRequestPermissionArray = new String[needRequestPermission.size()];
        needRequestPermission.toArray(needRequestPermissionArray);
        return needRequestPermissionArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isAllPermissionsGranted = true;
        if (requestCode != REQUEST_CODE || permissions == null || grantResults == null ||
                permissions.length == 0 || grantResults.length == 0) {
            isAllPermissionsGranted = false;
        } else {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED)
                    isAllPermissionsGranted = false;
            }
        }

        if (isAllPermissionsGranted) {
            if (mPreviewIntent != null) {
                if (mIsFromPreview) {
                    startActivity(mPreviewIntent);
                    setResult(Activity.RESULT_OK);
                } else {
                    if (mPreviewIntent != null) {
                        startActivity(mPreviewIntent);
                    }
                }
                finish();
            } else {
                showMissingPermissionDialog();
            }
        }
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_help);
        builder.setMessage(R.string.dialog_content);
        builder.setNegativeButton(R.string.dialog_button_quit,
                new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        if (mIsFromPreview){
                            setResult(Activity.RESULT_CANCELED);
                        }
                        finish();
                    }
                });
        builder.setPositiveButton(R.string.dialog_button_settings,
                new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                        startActivity(intent);
                        finish();
                    }
                });
        builder.show();
    }

    public static boolean checkAndRequestPermission(Activity activity, String[] permissions) {
        String[] neededPermissions = checkRequestedPermission(activity, permissions);
        if (neededPermissions.length == 0) {
            return false;
        } else {
            Intent intent = new Intent();
            intent.setClass(activity, PermissionActivity.class);
            intent.putExtra(REQUEST_PERMISSIONS, permissions);
            intent.putExtra(PREVIOUS_INTENT, activity.getIntent());
            activity.startActivity(intent);
            activity.finish();
            return true;
        }
    }
}
