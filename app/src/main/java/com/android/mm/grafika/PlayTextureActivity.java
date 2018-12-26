package com.android.mm.grafika;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.android.mm.R;
import com.android.mm.grafika.utils.MiscUtils;
import com.android.mm.grafika.utils.MoviePlayer;
import com.android.mm.grafika.utils.SpeedControlCallback;

import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;

/**
 * Play a movie from a file on disk.  Output goes to a TextureView.
 * <p>
 * Currently video-only.
 * <p>
 * Contrast with PlayMovieSurfaceActivity, which uses a SurfaceView.  Much of the code is
 * the same, but here we can handle the aspect ratio adjustment with a simple matrix,
 * rather than a custom layout.
 * <p>
 * TODO: investigate crash when screen is rotated while movie is playing (need
 *       to have onPause() wait for playback to stop)
 *
 * Activity中包含一个TextureView,通过
 * SurfaceTexture st = mTextureView.getSurfaceTexture();
 * Surface surface = new Surface(st);
 * 获取到Serface，然后传递给MoviePlayer进行播放。
 */
public class PlayTextureActivity extends Activity implements AdapterView.OnItemSelectedListener,
        TextureView.SurfaceTextureListener, MoviePlayer.PlayerFeedback {

    private static final String TAG = GrafikaActivity.TAG;

    private TextureView mTextureView;
    private String[] mMovieFiles;
    private int mSelectedMovie;
    private boolean mShowStopLabel;
    private MoviePlayer.PlayTask mPlayTask;
    private boolean mSurfaceTextureReady = false;

    private final Object mStopper = new Object();   // used to signal stop

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_texture_play);

        mTextureView = findViewById(R.id.movieTextureView);
        mTextureView.setSurfaceTextureListener(this);

        // Populate file-selection spinner.
        Spinner spinner = (Spinner) findViewById(R.id.playMovieFileSpinner);
        // Need to create one of these fancy ArrayAdapter thingies, and specify the generic layout
        // for the widget itself.
        mMovieFiles = MiscUtils.getFiles(getFilesDir(), "*.mp4");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mMovieFiles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        updateControls();
    }

    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
       Button play = findViewById(R.id.playStopButton);
       if (mShowStopLabel) {
           play.setText(R.string.stop);
       } else {
           play.setText(R.string.play);
       }
       play.setEnabled(mSurfaceTextureReady);

        // We don't support changes mid-play, so dim these.
        CheckBox check = (CheckBox) findViewById(R.id.locked60fpsCheckBox);
        check.setEnabled(!mShowStopLabel);
        check = (CheckBox) findViewById(R.id.loopPlaybackCheckBox);
        check.setEnabled(!mShowStopLabel);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "PlayMovieActivity onPause");
        super.onPause();
        // We're not keeping track of the state in static fields, so we need to shut the
        // playback down.  Ideally we'd preserve the state so that the player would continue
        // after a device rotation.
        //
        // We want to be sure that the player won't continue to send frames after we pause,
        // because we're tearing the view down.  So we wait for it to stop here.
        if (mPlayTask != null) {
            stopPlayback();
            mPlayTask.waitForStop();
        }
    }

    /**
     * Requests stoppage if a movie is currently playing.  Does not wait for it to stop.
     */
    private void stopPlayback() {
        if (mPlayTask != null) {
            mPlayTask.requestStop();
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        mSelectedMovie = spinner.getSelectedItemPosition();

        Log.d(TAG, "onItemSelected: " + mSelectedMovie + " '" + mMovieFiles[mSelectedMovie] + "'");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing to do
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        // There's a short delay between the start of the activity and the initialization
        // of the SurfaceTexture that backs the TextureView.  We don't want to try to
        // send a video stream to the TextureView before it has initialized, so we disable
        // the "play" button until this callback fires.
        Log.d(TAG, "SurfaceTexture ready (" + width + "x" + height + ")");
        mSurfaceTextureReady = true;
        updateControls();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // ignore
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurfaceTextureReady = false;
        // assume activity is pausing, so don't need to update controls.
        // Activity是被回调者。
        return true;        // caller should release ST.
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // ignore
    }

    @Override       // MoviePlayer.PlayerFeedback
    public void playbackStopped() {
        Log.d(TAG, "playback stopped");
        mShowStopLabel = false;
        mPlayTask = null;
        updateControls();
    }

    /**
     * onClick handler for "play/stop" button.
     */
    public void clickPlayStop(View view) {
        Log.d(TAG, "clickPlayStop");
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
            Log.d(TAG, "starting movie");
            SpeedControlCallback callback = new SpeedControlCallback();
            if (((CheckBox) findViewById(R.id.locked60fpsCheckBox)).isChecked()) {
                // TODO: consider changing this to be "free running" mode
                callback.setFixedPlaybackRate(60);
            }

            SurfaceTexture st = mTextureView.getSurfaceTexture();
            Surface surface = new Surface(st);

            MoviePlayer player = null;
            try {
                player = new MoviePlayer(
                        new File(getFilesDir(), mMovieFiles[mSelectedMovie]), surface, callback);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to play movie", ioe);
                surface.release();
                return;
            }

            adjustAspectRatio(player.getVideoWidth(), player.getVideoHeight());

            mPlayTask = new MoviePlayer.PlayTask(player, this);
            if (((CheckBox) findViewById(R.id.loopPlaybackCheckBox)).isChecked()) {
                mPlayTask.setLoopMode(true);
            }
            mShowStopLabel = true;
            updateControls();
            mPlayTask.execute();
        }
    }

    /**
     * Sets the TextureView transform to preserve the aspect ratio of the video.
     */
    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        int viewWidth = mTextureView.getWidth();
        int viewHeight = mTextureView.getHeight();

        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            // limited by short height; restrict width
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;
        Log.v(TAG, "video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff);

        Matrix txform = new Matrix();
        mTextureView.getTransform(txform);

        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        //txform.postRotate(10);          // just for fun
        txform.postTranslate(xoff, yoff);
        mTextureView.setTransform(txform);
    }
}
