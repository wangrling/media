package com.android.mm.sonic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.mm.R;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;

/**
 * 通过改变speed的方式听起来更舒服。
 */

public class SonicActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sonic);
    }

    public void clickPlay(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final EditText speedEdit = (EditText) findViewById(R.id.speed);
                final EditText pitchEdit = (EditText) findViewById(R.id.pitch);
                final EditText rateEdit = (EditText) findViewById(R.id.rate);
                float speed = Float.parseFloat(speedEdit.getText().toString());
                float pitch = Float.parseFloat(pitchEdit.getText().toString());
                float rate = Float.parseFloat(rateEdit.getText().toString());

                AudioDevice device = new AudioDevice(22050, 1);
                Sonic sonic = new Sonic(22050, 1);
                byte samples[] = new byte[4096];
                byte modifiedSamples[] = new byte[2048];

                InputStream soundFile = getResources().openRawResource(R.raw.talking);
                int bytesRead;
                if (soundFile != null) {
                    sonic.setSpeed(speed);
                    sonic.setPitch(pitch);
                    sonic.setRate(rate);
                    do {
                        try {
                            // 理论上读取4096个字节到samples中，实际读取bytesRead个字节。
                            bytesRead = soundFile.read(samples, 0, samples.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return ;
                        }
                        if (bytesRead > 0) {
                            sonic.putBytes(samples, bytesRead);
                        }
                        int available = sonic.availableBytes();
                        if (available > 0) {
                            if (modifiedSamples.length < available) {
                                modifiedSamples = new byte[available * 2];
                            }
                            sonic.receiveBytes(modifiedSamples, available);
                            device.writeSamples(modifiedSamples, available);
                        }
                    } while (bytesRead > 0);
                    device.flush();
                }
            }
        }).start();
    }
}
