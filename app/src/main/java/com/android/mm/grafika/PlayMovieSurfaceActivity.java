package com.android.mm.grafika;

import android.app.Activity;
import android.opengl.GLES30;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.mm.R;
import com.android.mm.grafika.gles.EglCore;
import com.android.mm.grafika.gles.WindowSurface;
import com.android.mm.grafika.utils.AspectFrameLayout;
import com.android.mm.grafika.utils.MiscUtils;
import com.android.mm.grafika.utils.MoviePlayer;
import com.android.mm.grafika.utils.SpeedControlCallback;

import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;

public class PlayMovieSurfaceActivity extends Activity implements
        SurfaceHolder.Callback, AdapterView.OnItemSelectedListener,
        MoviePlayer.PlayerFeedback {

    private static final String TAG = GrafikaActivity.TAG;

    private SurfaceView mSurfaceView;
    private String[] mMovieFiles;
    private int mSelectedMovie;

    private boolean mShowStopLabel;

    private MoviePlayer.PlayTask mPlayTask;

    // 是否获取到SurfaceTexture
    private boolean mSurfaceHolderReady = false;

    /**
     * Overridable  method to get layout id.  Any provided layout needs to include
     * the same views (or compatible) as active_play_movie_surface
     */
    // 为ScreenRecordActivity作准备。
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
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        // We're not keeping track of the state in static fields, so we need to shut the
        // playback down.  Ideally we'd preserve the state so that the player would continue
        // after a device rotation.
        //
        // We want to be sure that the player won't continue to send frames after we pause,
        // because we're tearing the view down.  So we wait for it to stop here.
        if (mPlayTask != null);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // There's a short delay between the start of the activity and the initialization
        // of the SurfaceHolder that backs the SurfaceView.  We don't want to try to
        // send a video stream to the SurfaceView before it has initialized, so we disable
        // the "play" button until this callback fires.
        Log.d(TAG, "surfaceCreated");
        mSurfaceHolderReady = true;

        updateControls();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // ignore
        Log.d(TAG, "surfaceChanged fmt=" + format + " size=" + width + "x" + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ignore
        Log.d(TAG, "Surface destroyed");
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

    // button click
    // onClick handler for "play/stop" button.
    public void clickPlayStop(View view) {
        if (mShowStopLabel) {
            Log.d(TAG, "stopping movie");
            stopPlayback();
            // Don't update the controls here -- let the task thread do it after the movie has
            // actually stopped.
            //mShowStopLabel = false;
            //updateControls();
        } else {
            if (mPlayTask != null) {
                Log.w(TAG, "movie already playing");
                return;
            }

            // 创建播放任务
            Log.d(TAG, "starting movie");
            SpeedControlCallback callback = new SpeedControlCallback();
            // holder很快就可以获取
            SurfaceHolder holder = mSurfaceView.getHolder();
            Surface surface = holder.getSurface();

            // Don't leave the last frame of the previous video hanging on the screen.
            // Looks weird if the aspect ratio changes.
            clearSurface(surface);

            MoviePlayer player = null;

            try {
                player = new MoviePlayer(new File(getFilesDir(), mMovieFiles[mSelectedMovie]),
                        surface ,callback);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to play movie", ioe);
                surface.release();
                return;
            }

            AspectFrameLayout layout = findViewById(R.id.playMovieAFL);
            int width = player.getVideoWidth();
            int height = player.getVideoHeight();
            layout.setAspectRatio((double)width/height);

            mPlayTask = new MoviePlayer.PlayTask(player, this);

            mShowStopLabel = true;

            updateControls();

            mPlayTask.execute();
        }
    }

    /**
     * Clears the playback surface to black.
     */
    private void clearSurface(Surface surface) {
        // We need to do this with OpenGL ES (*not* Canvas -- the "software render" bits
        // are sticky).  We can't stay connected to the Surface after we're done because
        // that'd prevent the video encoder from attaching.
        //
        // If the Surface is resized to be larger, the new portions will be black, so
        // clearing to something other than black may look weird unless we do the clear
        // post-resize.
        EglCore eglCore = new EglCore();
        WindowSurface win = new WindowSurface(eglCore, surface, false);
        win.makeCurrent();
        // 黑屏
        GLES30.glClearColor(0, 0, 0, 0);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        win.swapBuffers();
        win.release();
        eglCore.release();
    }

    /**
     * Requests stoppage if a movie is currently playing.
     */
    private void stopPlayback() {
        if (mPlayTask != null) {
            mPlayTask.requestStop();
        }
    }

    @Override   // MoviePlayer.PlayerFeedback
    public void playbackStopped() {
        Log.d(TAG, "playback stopped");
        mShowStopLabel = false;
        mPlayTask = null;
        updateControls();
    }
}
