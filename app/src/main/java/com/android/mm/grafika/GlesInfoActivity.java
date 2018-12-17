package com.android.mm.grafika;

import android.app.Activity;
import android.opengl.EGL14;
import android.opengl.GLES30;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mm.R;
import com.android.mm.grafika.gles.EglCore;
import com.android.mm.grafika.gles.OffscreenSurface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import androidx.annotation.Nullable;

/**
 * Simple activity that gathers and displays information from the GLES driver.
 * Dumps version info and extension lists.
 * - The "Save" button writes a copy of the output to the app's file area.
 */

public class GlesInfoActivity extends Activity {
    private static final String TAG = GrafikaActivity.TAG;

    private String mGlInfo;
    private File mOutputFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gles_info);

        mOutputFile = new File(getFilesDir(), "gles-info.txt");
        TextView tv = findViewById(R.id.glesInfoFileText);
        tv.setText(mOutputFile.toString());

        mGlInfo = gatherGlInfo();

        tv = findViewById(R.id.glesInfoText);
        tv.setText(mGlInfo);
    }


    /**
     * onClick handler for "save" button.
     */
    public void clickSave(View view) {
        try {
            FileWriter writer = new FileWriter(mOutputFile);
            writer.write(mGlInfo);
            writer.close();
            Log.d(TAG, "Output written to '" + mOutputFile + "'");
            Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            Log.e(TAG, "Failed writing file", ioe);
            // TODO: notify the user, not just logcat
        }
    }

    /**
     * Queries EGL/GL for information, then formats it all into one giant string.
     */
    private String gatherGlInfo() {
        // We need a GL context to examine, which means we need an EGL surface.  Create a 1x1
        // pbuffer.
        EglCore eglCore = new EglCore(null, EglCore.FLAG_TRY_GLES3);
        OffscreenSurface surface = new OffscreenSurface(eglCore, 1, 1);
        surface.makeCurrent();

        StringBuilder sb = new StringBuilder();
        sb.append("===== GL Information =====");
        sb.append("\nvendor    : ");
        sb.append(GLES30.glGetString(GLES30.GL_VENDOR));
        sb.append("\nversion   : ");
        sb.append(GLES30.glGetString(GLES30.GL_VERSION));
        sb.append("\nrenderer  : ");
        sb.append(GLES30.glGetString(GLES30.GL_RENDERER));
        sb.append("\nextensions:\n");
        sb.append(formatExtensions(GLES30.glGetString(GLES30.GL_EXTENSIONS)));

        sb.append("\n===== EGL Information =====");
        sb.append("\nvendor    : ");
        // elgCore属于OffscreenSurface成员变量。
        sb.append(eglCore.queryString(EGL14.EGL_VENDOR));
        sb.append("\nversion   : ");
        sb.append(eglCore.queryString(EGL14.EGL_VERSION));
        sb.append("\nclient API: ");
        sb.append(eglCore.queryString(EGL14.EGL_CLIENT_APIS));
        sb.append("\nextensions:\n");
        sb.append(formatExtensions(eglCore.queryString(EGL14.EGL_EXTENSIONS)));

        surface.release();
        eglCore.release();

        sb.append("\n===== System Information =====");
        sb.append("\nmfgr      : ");
        sb.append(Build.MANUFACTURER);
        sb.append("\nbrand     : ");
        sb.append(Build.BRAND);
        sb.append("\nmodel     : ");
        sb.append(Build.MODEL);
        sb.append("\nrelease   : ");
        sb.append(Build.VERSION.RELEASE);
        sb.append("\nbuild     : ");
        sb.append(Build.DISPLAY);
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Formats the extensions string, which is a space-separated list, into a series of indented
     * values followed by newlines. The list is sorted.
     */
    private String formatExtensions(String ext) {
        String[] values = ext.split(" ");
        Arrays.sort(values);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append("  ");
            sb.append(values[i]);
            sb.append("\n");
        }
        return sb.toString();
    }
}
