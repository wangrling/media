package com.android.mm.webrtc;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mm.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;

/**
 * Handles the initial setup where the user selects which room to join.
 */

public class WebRtcActivity extends Activity {

    private static final String TAG = "WebRtcActivity";
    private static final int CONNECTION_REQUEST = 1;
    private static boolean commandLineRun = false;

    private ImageButton addRoomButton;
    private ImageButton removeRoomButton;
    private ImageButton connectButton;
    private ImageButton connectLoopbackButton;
    private EditText roomEditText;
    private ListView roomListView;
    private SharedPreferences sharedPref;
    private String keyprefVideoCallEnabled;
    private String keyprefResolution;
    private String keyprefFps;
    private String keyprefCaptureQualitySlider;
    private String keyprefVideoBitrateType;
    private String keyprefVideoBitrateValue;
    private String keyprefVideoCodec;
    private String keyprefAudioBitrateType;
    private String keyprefAudioBitrateValue;
    private String keyprefAudioCodec;
    private String keyprefHwCodecAcceleration;
    private String keyprefCaptureToTexture;
    private String keyprefNoAudioProcessingPipeline;
    private String keyprefAecDump;
    private String keyprefOpenSLES;
    private String keyprefDisplayHud;
    private String keyprefTracing;
    private String keyprefRoomServerUrl;
    private String keyprefRoom;
    private String keyprefRoomList;
    private ArrayList<String> roomList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get setting keys.
        PreferenceManager.setDefaultValues(this, R.xml.webrtc_pref, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        keyprefVideoCallEnabled = getString(R.string.pref_video_call_key);
        keyprefResolution = getString(R.string.pref_resolution_key);
        keyprefFps = getString(R.string.pref_fps_key);
        keyprefCaptureQualitySlider = getString(R.string.pref_capture_quality_slider_key);
        keyprefVideoBitrateType = getString(R.string.pref_start_video_bitrate_key);
        keyprefVideoBitrateValue = getString(R.string.pref_start_video_bitrate_value_key);
        keyprefVideoCodec = getString(R.string.pref_video_codec_key);
        keyprefHwCodecAcceleration = getString(R.string.pref_hw_codec_key);
        keyprefCaptureToTexture = getString(R.string.pref_capture_to_texture_key);
        keyprefAudioBitrateType = getString(R.string.pref_start_audio_bitrate_key);
        keyprefAudioBitrateValue = getString(R.string.pref_start_audio_bitrate_value_key);
        keyprefAudioCodec = getString(R.string.pref_audio_codec_key);
        keyprefNoAudioProcessingPipeline = getString(R.string.pref_no_audio_processing_key);
        keyprefAecDump = getString(R.string.pref_aec_dump_key);
        keyprefOpenSLES = getString(R.string.pref_opensles_key);
        keyprefDisplayHud = getString(R.string.pref_display_hud_key);
        keyprefTracing = getString(R.string.pref_tracing_key);
        keyprefRoomServerUrl = getString(R.string.pref_room_server_url_key);
        keyprefRoom = getString(R.string.pref_room_key);
        keyprefRoomList = getString(R.string.pref_room_list_key);

        setContentView(R.layout.activity_webrtc);

        setActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        roomEditText = findViewById(R.id.roomEditText);
        roomEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addRoomButton.performClick();
                    return true;
                }
                return false;
            }
        });
        roomEditText.requestFocus();

        roomListView = findViewById(R.id.roomListView);
        roomListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        addRoomButton = findViewById(R.id.addRoomButton);
        addRoomButton.setOnClickListener(addRoomListener);
        removeRoomButton = (ImageButton) findViewById(R.id.removeRoomButton);
        removeRoomButton.setOnClickListener(removeRoomListener);
        connectButton = (ImageButton) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(connectListener);
        connectLoopbackButton =
                (ImageButton) findViewById(R.id.connectLoopbackButton);
        connectLoopbackButton.setOnClickListener(connectListener);

        // If an implicit VIEW intent is launching the app, go directly to that URL.
        final Intent intent = getIntent();
        if ("android.intent.action.VIEW".equals(intent.getAction())
                && !commandLineRun) {
            commandLineRun = true;
            boolean loopback = intent.getBooleanExtra(
                    CallActivity.EXTRA_LOOPBACK, false);
            int runTimeMs = intent.getIntExtra(
                    CallActivity.EXTRA_RUNTIME, 0);
            String room = sharedPref.getString(keyprefRoom, "");
            roomEditText.setText(room);
            connectToRoom(loopback, runTimeMs);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items.
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private final View.OnClickListener connectListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean loopback = false;
            if (view.getId() == R.id.connectLoopbackButton) {
                loopback = true;
            }
            commandLineRun = false;
            connectToRoom(loopback, 0);
        }
    };

    private void connectToRoom(boolean loopback, int runTimeMs) {

    }

    private final View.OnClickListener addRoomListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String newRoom = roomEditText.getText().toString();
            if (newRoom.length() > 0 && !roomList.contains(newRoom)) {
                adapter.add(newRoom);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private final View.OnClickListener removeRoomListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String selectedRoom = getSelectedItem();
            if (selectedRoom != null) {
                adapter.remove(selectedRoom);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private String getSelectedItem() {
        int position = AdapterView.INVALID_POSITION;
        if (roomListView.getCheckedItemCount() > 0 && adapter.getCount() > 0) {
            position = roomListView.getCheckedItemPosition();
            if (position >= adapter.getCount()) {
                position = AdapterView.INVALID_POSITION;
            }
        }
        if (position != AdapterView.INVALID_POSITION) {
            return adapter.getItem(position);
        } else {
            return null;
        }
    }
}