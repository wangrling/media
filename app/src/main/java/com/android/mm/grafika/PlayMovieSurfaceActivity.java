package com.android.mm.grafika;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.mm.R;
import com.android.mm.grafika.utils.MiscUtils;

import androidx.annotation.Nullable;

public class PlayMovieSurfaceActivity extends Activity implements
        SurfaceHolder.Callback, AdapterView.OnItemSelectedListener {

    private static final String TAG = GrafikaActivity.TAG;

    private SurfaceView mSurfaceView;
    private String[] mMovieFiles;
    private int mSelectedMovie;

    private boolean mShowStopLabel;

    // 是否获取到SurfaceTexture
    private boolean mSurfaceHolderReady = false;

    /**
     * Overridable  method to get layout id.  Any provided layout needs to include
     * the same views (or compatible) as active_play_movie_surface
     */
    protected int getContentViewId() {
        return R.layout.activity_play_movie_surface;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());

        mSurfaceView = findViewById(R.id.playMovieSurface);
        mSurfaceView.getHolder().addCallback(this);

        // Populate file-selection spinner.
        Spinner spinner = findViewById(R.id.playMovieFileSpinner);
        // Need to create one of these fancy ArrayAdapter thingies, and specify the generic layout
        // for the widget itself.
        mMovieFiles = MiscUtils.getFiles(getFilesDir(), "*.mp4");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mMovieFiles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        updateControls();
    }

    // Updates the on-screen controls to reflect the current state of the app.
    private void updateControls() {
        Button play = findViewById(R.id.playStopButton);
        if (mShowStopLabel) {
            play.setText(R.string.stop);
        } else {
            play.setText(R.string.play);
        }
        play.setEnabled(mSurfaceHolderReady);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        mSelectedMovie = spinner.getSelectedItemPosition();

        Log.d(TAG, "onItemSelected: " + mSelectedMovie + " '" + mMovieFiles[mSelectedMovie] + "'");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
