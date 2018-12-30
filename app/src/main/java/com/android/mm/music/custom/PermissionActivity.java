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

public class PermissionActivity extends Activity{
    private static final String PERVIOUS_INTENT = "pervious_intent";
    private static final String REQUEST_PERMISSIONS = "request_permissions";
    private static final String KEY_FROM_PREVIEW = "from_preview";
    private static final String PACKAGE_URL_SCHEME = "package:";
    private static final int REQUEST_CODE = 100;
    private String[] mRequestedPermissons;
    private Intent mPreviousIntent;
    private boolean mIsFromPreview = false;

    public static void startFromPreview(Activity activity, String[] permissions, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, PermissionActivity.class);
        intent.putExtra(REQUEST_PERMISSIONS, permissions);
        intent.putExtra(PERVIOUS_INTENT, activity.getIntent());
        intent.putExtra(KEY_FROM_PREVIEW, true);
        activity.startActivityForResult(intent, requestCode);
    }

    public static boolean checkAndRequestPermission(Activity activity, String[] permissions) {
        String[] neededPermissions = checkRequestedPermission(activity, permissions);
        if (neededPermissions.length == 0) {
            return false;
        } else {
            Intent intent = new Intent();
            intent.setClass(activity, PermissionActivity.class);
            intent.putExtra(REQUEST_PERMISSIONS, permissions);
            intent.putExtra(PERVIOUS_INTENT, activity.getIntent());
            activity.startActivity(intent);
            activity.finish();
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreviousIntent = (Intent)getIntent().getExtras().get(PERVIOUS_INTENT);
        mIsFromPreview = getIntent().getBooleanExtra(KEY_FROM_PREVIEW, false);
        mRequestedPermissons = getIntent().getStringArrayExtra(REQUEST_PERMISSIONS);
        if (savedInstanceState == null && mRequestedPermissons != null) {
            String[] neededPermissions =
                    checkRequestedPermission(PermissionActivity.this, mRequestedPermissons);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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
        if(isAllPermissionsGranted) {
            if (mPreviousIntent != null)
                if (mIsFromPreview){
                    startActivity(mPreviousIntent);
                    setResult(Activity.RESULT_OK);
                } else {
                    if (mPreviousIntent != null)
                        startActivity(mPreviousIntent);
                }
            finish();
        } else {
            showMissingPermissionDialog();
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
}
