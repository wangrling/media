package com.android.mm.mediacts;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import android.util.Log;


import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.media.AudioFormat.ENCODING_INVALID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AudioTrackTest {
    private String TAG = "AudioTrackTest";
    private final long WAIT_MSEC = 200;
    private final int OFFSET_DEFAULT = 0;
    private final int OFFSET_NEGATIVE = -10;

    private void log(String testName, String message) {
        Log.v(TAG, "[" + testName + "] " + message);
    }

    private void loge(String testName, String message) {
        Log.e(TAG, "[" + testName + "] " + message);
    }


    // private class to hold test results.
    private static class TestResults {
        public boolean mResult = false;
        public String mResultLog = "";

        public TestResults(boolean b, String s) {
            mResult = b;
            mResultLog = s;
        }
    }


    // generic test methods

    /**
     * @param _inTest_streamType     流类型，有音乐流，提示音流，闹钟流，电话流等。
     * @param _inTest_mode           static表示数据一次性传输，stream表示数据以流的形式传输。
     * @param _inTest_config         左前，右前，左后，右后等方位信息。
     * @param _inTest_format         编码格式，一般为pcm数据。
     * @param _expected_stateForMode 期望的状态，初始化，未初始化，没有静态数据。
     * @return
     */
    private TestResults constructorTestMultiSampleRate(
            // parameters tested by this method
            int _inTest_streamType, int _inTest_mode, int _inTest_config, int _inTest_format,
            // parameter-dependent expected results
            int _expected_stateForMode) {

        // 支持的比特率
        int[] testSampleRates = {8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000};
        String failedRates = "Failure for rate(s): ";
        boolean localRes, finalRes = true;

        for (int i = 0; i < testSampleRates.length; i++) {
            AudioTrack track = null;
            try {
                track = new AudioTrack(_inTest_streamType, testSampleRates[i], _inTest_config,
                        _inTest_format, AudioTrack.getMinBufferSize(testSampleRates[i],
                        _inTest_config, _inTest_format), _inTest_mode);
            } catch (IllegalArgumentException iae) {
                Log.e("MediaAudioTrackTest", "[ constructorTestMultiSampleRate ] exception at SR "
                        + testSampleRates[i] + ": \n" + iae);
                localRes = false;
            }
            if (track != null) {
                localRes = (track.getState() == _expected_stateForMode);
                track.release();
            } else {
                localRes = false;
            }

            if (!localRes) {
                // log the error for the test runner
                failedRates += Integer.toString(testSampleRates[i]) + "Hz ";
                // log the error for logcat
                log("constructorTestMultiSampleRate", "failed to construct "
                        + "AudioTrack(streamType="
                        + _inTest_streamType
                        + ", sampleRateInHz="
                        + testSampleRates[i]
                        + ", channelConfig="
                        + _inTest_config
                        + ", audioFormat="
                        + _inTest_format
                        + ", bufferSizeInBytes="
                        + AudioTrack.getMinBufferSize(testSampleRates[i], _inTest_config,
                        AudioFormat.ENCODING_PCM_16BIT) + ", mode=" + _inTest_mode);
                // mark test as failed
                finalRes = false;
            }
        }
        // 检测finalRes的结果。
        return new TestResults(finalRes, failedRates);
    }

    // ------------------------------------------------------------
    // AUDIOTRACK TESTS:
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // AudioTrack constructor and AudioTrack.getMinBufferSize(...) for 16bit PCM.
    // ------------------------------------------------------------

    // Test case 1: constructor for streaming AudioTrack, mono, 16bit at misc
    // valid sample rates.
    @Test
    public void testConstructorMono16MusicStream() throws Exception {

        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STREAM, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, AudioTrack.STATE_INITIALIZED);

        assertTrue("testConstructorMono16MusicStream: " + res.mResultLog, res.mResult);
    }

    // Test case 2: constructor for streaming AudioTrack, stereo, 16 bit at misc
    // valid sample rates.
    @Test
    public void testConstructorStereo16MusicStream() throws Exception {
        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STREAM, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, AudioTrack.STATE_INITIALIZED);

        assertTrue("testConstructorStereo16MusicStream: " + res.mResultLog, res.mResult);
    }

    // Test case 3: constructor for static AudioTrack, mono, 16bit at misc valid
    // sample rates.
    @Test
    public void testConstructorMono16MusicStatic() throws Exception {
        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STATIC, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, AudioTrack.STATE_NO_STATIC_DATA);

        assertTrue("testConstructorMono16MusicStatic: " + res.mResultLog, res.mResult);
    }

    // Test case 4: constructor for static AudioTrack, stereo, 16bit at misc
    // valid sample rates.
    @Test
    public void testConstructorStereo16MusicStatic() throws Exception {
        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STATIC, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, AudioTrack.STATE_NO_STATIC_DATA);
        assertTrue("testConstructorStereo16MusicStatic: " + res.mResultLog, res.mResult);
    }

    // ------------------------------------------------------------
    // AudioTrack constructor and AudioTrack.getMinBufferSize(...) for 8 bit PCM.
    // ------------------------------------------------------------

    // Test case 1: constructor for streaming AudioTrack, mono, 8bit at misc
    // valid sample rates.
    @Test
    public void testConstructorMono8MusicStream() throws Exception {
        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STREAM, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, AudioTrack.STATE_INITIALIZED);

        assertTrue("testConstructorMono8MusicStream: " + res.mResultLog, res.mResult);
    }

    // Test case 2: constructor for streaming AudioTrack, stereo, 8bit at misc
    // valid sample rates.
    @Test
    public void testConstructorStereo8MusicStream() throws Exception {
        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STREAM, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_8BIT, AudioTrack.STATE_INITIALIZED);
        assertTrue("testConstructorStereo8MusicStream: " + res.mResultLog, res.mResult);
    }

    // Test case 3: constructor for static AudioTrack, mono, 8bit at misc valid
    // sample rates
    @Test
    public void testConstructorMono8MusicStatic() throws Exception {
        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STATIC, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, AudioTrack.STATE_NO_STATIC_DATA);
        assertTrue("testConstructorMono8MusicStatic: " + res.mResultLog, res.mResult);
    }

    // Test case 4: constructor for static AudioTrack, stereo, 8bit at misc
    // valid sample rates
    @Test
    public void testConstructorStereo8MusicStatic() throws Exception {
        TestResults res = constructorTestMultiSampleRate(AudioManager.STREAM_MUSIC,
                AudioTrack.MODE_STATIC, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_8BIT, AudioTrack.STATE_NO_STATIC_DATA);
        assertTrue("testConstructorStereo8MusicStatic: " + res.mResultLog, res.mResult);
    }

    // ------------------------------------------------------------
    // AudioTrack constructor for all stream types
    // ------------------------------------------------------------

    // Test case 1: constructor for all stream types.
    @Test
    public void testConstructorStreamType() throws Exception {
        // constants for test
        // 比特率
        final int TYPE_TEST_SR = 22050;
        final int TYPE_TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TYPE_TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TYPE_TEST_MODE = AudioTrack.MODE_STREAM;
        final int[] STREAM_TYPES = {
                AudioManager.STREAM_ALARM, AudioManager.STREAM_MUSIC,
                AudioManager.STREAM_NOTIFICATION, AudioManager.STREAM_RING,
                AudioManager.STREAM_SYSTEM, AudioManager.STREAM_VOICE_CALL
        };
        final String[] STREAM_NAMES = {"STREAM_ALARM", "STREAM_MUSIC", "STREAM_NOTIFICATION",
                "STREAM_RING", "STREAM_SYSTEM", "STREAM_VOICE_CALL"};

        boolean localTestRes = true;
        AudioTrack track = null;
        // test: loop constructor on all stream types.
        for (int i = 0; i < STREAM_TYPES.length; i++) {
            try {
                // -------- initialization --------------
                track = new AudioTrack(STREAM_TYPES[i], TYPE_TEST_SR, TYPE_TEST_CONF,
                        TYPE_TEST_FORMAT, AudioTrack.getMinBufferSize(TYPE_TEST_SR, TYPE_TEST_CONF,
                        TYPE_TEST_FORMAT), TYPE_TEST_MODE);

            } catch (IllegalArgumentException iae) {
                loge("testConstructorStreamType", "exception for stream type " + STREAM_NAMES[i]
                        + ": " + iae);
                localTestRes = false;
            }
            // -------- test --------------
            if (track != null) {
                if (track.getState() != AudioTrack.STATE_INITIALIZED) {
                    localTestRes = false;
                    Log.e("MediaAudioTrackTest",
                            "[ testConstructorStreamType ] failed for stream type "
                                    + STREAM_NAMES[i]);
                }
                // -------- tear down --------------
                track.release();
            } else {
                localTestRes = false;
            }
        }
        assertTrue("testConstructorStreamType", localTestRes);
    }

    // ------------------------------------------------------------
    // Playback head position
    // ------------------------------------------------------------

    // Test case 1: getPlaybackHeadPosition() at 0 after initialization.
    @Test
    public void testPlaybackHeadPositionAfterInit() throws Exception {
        // constants for test
        final String TEST_NAME = "testPlaybackHeadPositionAfterInit";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT), TEST_MODE);
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.getPlaybackHeadPosition() == 0);
        // -------- tear down --------------
        track.release();
    }

    // Test case 2: getPlaybackHeadPosition() increases after play().
    @Test
    public void testPlaybackHeadPositionIncrease() throws Exception {
        // constants for test
        // constants for test
        final String TEST_NAME = "testPlaybackHeadPositionIncrease";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.play();
        Thread.sleep(100);
        log(TEST_NAME, "position =" + track.getPlaybackHeadPosition());
        assertTrue(TEST_NAME, track.getPlaybackHeadPosition() > 0);
        // -------- tear down --------------
        track.release();
    }

    // Test case 3: getPlaybackHeadPosition() is 0 after flush();
    @Test
    public void testPlaybackHeadPositionAfterFlush() throws InterruptedException {
        // constants for test
        final String TEST_NAME = "testPlaybackHeadPositionAfterFlush";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.play();
        Thread.sleep(WAIT_MSEC);
        track.stop();
        track.flush();
        log(TEST_NAME, "position =" + track.getPlaybackHeadPosition());
        assertTrue(TEST_NAME, track.getPlaybackHeadPosition() == 0);
        // -------- tear down --------------
        track.release();
    }

    // Test case 3: getPlaybackHeadPosition() is 0 after stop();
    @Test
    public void testPlaybackHeadPositionAfterStop() throws Exception {
        // constants for test
        final String TEST_NAME = "testPlaybackHeadPositionAfterStop";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        final int TEST_LOOP_CNT = 10;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.play();
        Thread.sleep(WAIT_MSEC);
        track.stop();
        int count = 0;
        int pos;
        do {
            Thread.sleep(WAIT_MSEC);
            pos = track.getPlaybackHeadPosition();
            count++;
        } while ((pos != 0) && (count < TEST_LOOP_CNT));
        log(TEST_NAME, "position =" + pos + ", read count =" + count);
        assertTrue(TEST_NAME, pos == 0);
        // -------- tear down --------------
        track.release();
    }

    // Test case 4: getPlaybackHeadPosition() is > 0 after play(); pause();
    @Test
    public void testPlaybackHeadPositionAfterPause() throws Exception {
        // constants for test
        final String TEST_NAME = "testPlaybackHeadPositionAfterPause";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.play();
        Thread.sleep(100);
        track.pause();

        int pos = track.getPlaybackHeadPosition();

        log(TEST_NAME, "position =" + pos);
        assertTrue(TEST_NAME, pos > 0);
        // -------- tear down --------------
        track.release();
    }

    // Test case 5: getPlaybackHeadPosition() remains 0 after pause(); flush(); play();
    @Test
    public void testPlaybackHeadPositionAfterFlushAndPlay() throws Exception {
        // constants for test
        final String TEST_NAME = "testPlaybackHeadPositionAfterFlushAndPlay";
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        final int TEST_SR = AudioTrack.getNativeOutputSampleRate(TEST_STREAM_TYPE);
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);

        track.play();
        Thread.sleep(100);
        track.pause();

        int pos = track.getPlaybackHeadPosition();

        log(TEST_NAME, "position after pause =" + pos);
        assertTrue(TEST_NAME, pos > 0);

        track.flush();
        pos = track.getPlaybackHeadPosition();
        log(TEST_NAME, "position after flush =" + pos);
        assertTrue(TEST_NAME, pos == 0);

        // 没有数据play当然是0
        track.play();
        pos = track.getPlaybackHeadPosition();
        log(TEST_NAME, "position after play =" + pos);
        assertTrue(TEST_NAME, pos == 0);

        Thread.sleep(100);
        pos = track.getPlaybackHeadPosition();
        log(TEST_NAME, "position after 100 ms sleep =" + pos);
        assertTrue(TEST_NAME, pos == 0);
        // -------- tear down --------------
        track.release();
    }

    // ------------------------------------------------------------
    // Playback properties
    // ------------------------------------------------------------

    // Common code for the testSetStereoVolume* and testSetVolume* tests.
    private void testSetVolumeCommon(String testName, float vol, boolean isStereo) {
        // constants for test
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);

        byte[] data = new byte[minBuffSize];
        // -------- test --------------
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.play();
        // TODO to really test this, do a pan instead of using same value for left and right.
        assertTrue(testName, track.setVolume(vol) == AudioTrack.SUCCESS);
        track.release();
    }

    // Test case 1: setStereoVolume() with max volume returns SUCCESS.
    @Test
    public void testSetStereoVolumeMax() {
        final String TEST_NAME = "testSetStereoVolumeMax";
        float maxVol = AudioTrack.getMaxVolume();
        testSetVolumeCommon(TEST_NAME, maxVol, true /* isStereo */);
    }

    // Test case 2: setStereoVolume() with min volume returns SUCCESS.
    @Test
    public void testTestStereoVolumeMin() throws Exception {
        final String TEST_NAME = "testSetStereoVolumeMin";
        float minVol = AudioTrack.getMinVolume();
        testSetVolumeCommon(TEST_NAME, minVol, true /*isStereo*/);
    }

    // Test case 3: setStereoVolume() with mid volume returns SUCCESS.
    @Test
    public void testSetStereoVolumeMid() throws Exception {
        final String TEST_NAME = "testSetStereoVolumeMid";
        float midVol = (AudioTrack.getMaxVolume() - AudioTrack.getMinVolume()) / 2;
        testSetVolumeCommon(TEST_NAME, midVol, true /*isStereo*/);
    }

    // Test case 4: setPlaybackRate() with half the content rate return SUCCESS
    @Test
    public void testSetPlaybackRate() throws Exception {
        // constants for test
        final String TEST_NAME = "testSetPlaybackRate";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBufferSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBufferSize, TEST_MODE);
        byte data[] = new byte[minBufferSize];

        // -------- test --------------
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);

        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.play();
        // 时间延长一倍播放
        assertTrue(TEST_NAME, track.setPlaybackRate((int) (TEST_SR / 2)) == AudioTrack.SUCCESS);
    }

    // Test case 5: setPlaybackRate(0) returns bad value error
    @Test
    public void testSetPlaybackRateZero() throws Exception {
        // constants for test
        final String TEST_NAME = "testSetPlaybackRateZero";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.setPlaybackRate(0) == AudioTrack.ERROR_BAD_VALUE);
        // -------- tear down --------------
        track.release();
    }

    // Test case 6: setPlaybackRate() accepts value twice the output sample rate.
    @Test
    public void testSetPlaybackRateTwiceOutputSR() {
        // constants for test
        final String TEST_NAME = "testSetPlaybackRateTwiceOutputSR";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        int minBufferSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);

        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBufferSize, TEST_MODE);
        byte data[] = new byte[minBufferSize];
        int outputSR = AudioTrack.getNativeOutputSampleRate(TEST_STREAM_TYPE);
        // -------- test --------------
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.play();
        assertTrue(TEST_NAME, track.setPlaybackRate(2 * outputSR) == AudioTrack.SUCCESS);
        // -------- tear down --------------
        track.release();
    }

    // Test case 7: setPlaybackRate() and retrieve value, should be the same for
    // half the content SR.
    @Test
    public void testSetGetPlaybackRate() {
        // constants for test
        final String TEST_NAME = "testSetGetPlaybackRate";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.play();

        track.setPlaybackRate((int) (TEST_SR / 2));
        assertTrue(TEST_NAME, track.getPlaybackRate() == (int) (TEST_SR / 2));
        // -------- tear down --------------
        track.release();
    }

    // Test case 8: setPlaybackRate() invalid operation if track not initialized.
    @Test
    public void testSetPlaybackRateUninit() {
        // constants for test
        final String TEST_NAME = "testSetPlaybackRateUninit";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);

        // -------- test --------------
        assertEquals(TEST_NAME, AudioTrack.STATE_NO_STATIC_DATA, track.getState());
        assertEquals(TEST_NAME, AudioTrack.ERROR_INVALID_OPERATION, track.setPlaybackRate(TEST_SR / 2));

        // -------- tear down --------------
        track.release();
    }

    // Test case 9: setVolume() with max volume returns SUCCESS
    @Test
    public void testSetVolumeMax() {
        final String TEST_NAME = "testSetVolumeMax";
        float maxVol = AudioTrack.getMaxVolume();
        testSetVolumeCommon(TEST_NAME, maxVol, false /*isStereo*/);
    }

    // Test case 10: setVolume() with min volume returns SUCCESS
    @Test
    public void testSetVolumeMin() {
        final String TEST_NAME = "testSetVolumeMin";
        float minVol = AudioTrack.getMinVolume();
        testSetVolumeCommon(TEST_NAME, minVol, false /*isStereo*/);
    }

    // Test case 11: setVolume() with mid volume returns SUCCESS
    @Test
    public void testSetVolumeMid() throws Exception {
        final String TEST_NAME = "testSetVolumeMid";
        // float midVol = (AudioTrack.getMaxVolume() - AudioTrack.getMinVolume()) / 2;
        // 应该是加号
        float midVol = (AudioTrack.getMaxVolume() + AudioTrack.getMinVolume()) / 2;
        testSetVolumeCommon(TEST_NAME, midVol, false /*isStereo*/);
    }

    // -----------------------------------------------------------------
    // Playback progress
    // ----------------------------------

    // 在播放过程中无法设置PlaybackHeadPosition

    // Test case 1: setPlaybackHeadPosition() on playing track.
    @Test
    public void testSetPlaybackHeadPositionPlaying() {
        // constants for test
        final String TEST_NAME = "testSetPlaybackHeadPositionPlaying";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);

        track.play();
        assertTrue(TEST_NAME, track.setPlaybackHeadPosition(10) == AudioTrack.ERROR_INVALID_OPERATION);
        // -------- tear down --------------
        track.release();
    }

    // Test case 2: setPlaybackHeadPosition() on stopped track.
    @Test
    public void testSetPlaybackHeadPositionStopped() {
        // constants for test
        final String TEST_NAME = "testSetPlaybackHeadPositionStopped";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertEquals(TEST_NAME, AudioTrack.STATE_NO_STATIC_DATA, track.getState());
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertEquals(TEST_NAME, AudioTrack.STATE_INITIALIZED, track.getState());

        track.play();
        track.stop();
        assertEquals(TEST_NAME, AudioTrack.PLAYSTATE_STOPPED, track.getPlayState());
        assertEquals(TEST_NAME, AudioTrack.SUCCESS, track.setPlaybackHeadPosition(10));
        // -------- tear down --------------
        track.release();
    }

    // Test case 3: setPlaybackHeadPosition() on paused track.
    @Test
    public void testSetPlaybackHeadPositionPaused() {
        // constants for test
        final String TEST_NAME = "testSetPlaybackHeadPositionPaused";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertEquals(TEST_NAME, AudioTrack.STATE_NO_STATIC_DATA, track.getState());
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertEquals(TEST_NAME, AudioTrack.STATE_INITIALIZED, track.getState());
        track.play();
        track.pause();

        assertEquals(TEST_NAME, AudioTrack.PLAYSTATE_PAUSED, track.getPlayState());
        assertEquals(TEST_NAME, AudioTrack.SUCCESS, track.setPlaybackHeadPosition(10));

        // -------- tear down --------------
        track.release();
    }

    // 设置的位置超出buffer的大小。

    // Test case 4: setPlaybackHeadPosition() beyond what has been written.
    @Test
    public void testSetPlaybackHeadPositionTooFar() {
        // constants for test
        final String TEST_NAME = "testSetPlaybackHeadPositionTooFar";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];

        // make up a frame index that's beyond what has been written: go from
        // buffer size to frame.
        // count (given the audio track properties), and add 77.
        int frameIndexTooFor = (2 * minBuffSize / 2) + 77;

        // -------- test --------------
        assertEquals(TEST_NAME, AudioTrack.STATE_NO_STATIC_DATA, track.getState());
        track.write(data, OFFSET_DEFAULT, data.length);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertEquals(TEST_NAME, AudioTrack.STATE_INITIALIZED, track.getState());
        track.play();
        track.stop();

        assertEquals(TEST_NAME, AudioTrack.PLAYSTATE_STOPPED, track.getPlayState());
        assertEquals(TEST_NAME, AudioTrack.ERROR_BAD_VALUE,
                track.setPlaybackHeadPosition(frameIndexTooFor));

        // -------- tear down --------------
        track.release();
    }

    // Test case 5: setLoopPoints() fails for MODE_STREAM
    @Test
    public void testSetLoopPointsStream() throws Exception {
        // constants for test
        final String TEST_NAME = "testSetLoopPointsStream";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        // 构造数据
        byte data[] = new byte[minBuffSize];

        // -------- test --------------
        // 写数据
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        // 必须是静态数据，设置开始和结束的帧数，毕竟设置循环的次数。
        assertTrue(TEST_NAME, track.setLoopPoints(2, 50, 2) ==
                AudioTrack.ERROR_INVALID_OPERATION);

        // -------- tear down --------------
        track.release();
    }

    // Test case 6: setLoopPoints() fails start > end.
    @Test
    public void testSetLoopPointsStartAfterEnd() {
        // constants for test
        final String TEST_NAME = "testSetLoopPointsStartAfterEnd";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.setLoopPoints(50, 0, 2) ==
                AudioTrack.ERROR_BAD_VALUE);

        // -------- tear down --------------
        track.release();
    }

    // Test case 6: setLoopPoints() success
    public void testSetLoopPointsSuccess() {
        // constants for test
        final String TEST_NAME = "testSetLoopPointsSuccess";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.setLoopPoints(0, 50, 2) ==
                AudioTrack.SUCCESS);
        // -------- tear down --------------
        track.release();
    }

    // Test case 7: setLoopPoints() fails with loop length bigger than content.
    @Test
    public void testSetLoopPointLoopTooLong() {
        // constants for test
        final String TEST_NAME = "testSetLoopPointsLoopTooLong";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------

        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);

        byte data[] = new byte[minBuffSize];
        int dataSizeInFrames = minBuffSize / 2;
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_NO_STATIC_DATA);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        // 因为是16bit的帧。
        assertTrue(TEST_NAME, track.setLoopPoints(10, dataSizeInFrames + 20, 2) ==
                AudioTrack.ERROR_BAD_VALUE);

        // -------- tear down --------------
        track.release();
    }

    // Test case 8: setLoopPoints() fails with start beyond what can be written for the track.
    @Test
    public void testSetLoopPointsStartTooFar() {
        // constants for test
        final String TEST_NAME = "testSetLoopPointsStartTooFar";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        int dataSizeInFrames = minBuffSize / 2;// 16bit data

        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_NO_STATIC_DATA);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);

        assertTrue(TEST_NAME, track.setLoopPoints(dataSizeInFrames + 20, dataSizeInFrames + 50, 2) ==
                AudioTrack.ERROR_BAD_VALUE);
        // -------- tear down --------------
        track.release();
    }

    // Test case 9: setLoopPoints() fails with end beyond what can be written
    // for the track.
    @Test
    public void testSetLoopPointsEndTooFar() throws Exception {
        // constants for test
        final String TEST_NAME = "testSetLoopPointsEndTooFar";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        int dataSizeInFrames = minBuffSize / 2;// 16bit data
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_NO_STATIC_DATA);
        track.write(data, OFFSET_DEFAULT, data.length);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        int loopCount = 2;
        assertTrue(TEST_NAME, track.setLoopPoints(dataSizeInFrames - 10, dataSizeInFrames + 50, loopCount) ==
                AudioTrack.ERROR_BAD_VALUE);
        // -------- tear down --------------
        track.release();
    }

    // -----------------------------------------------------------------
    // Audio data supply
    // ----------------------------------

    // Test case 1: write() fails when supplying less data (bytes) than declared.
    @Test
    public void testWriteByteOffsetTooBig() {
        // constants for test
        final String TEST_NAME = "testWriteByteOffsetTooBig";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);

        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        int offset = 10;
        // 数据长度不够。
        assertTrue(TEST_NAME, track.write(data, offset, data.length) ==
                AudioTrack.ERROR_BAD_VALUE);
        // -------- tear down --------------
        track.release();
    }

    // 相比上个方法只是把bytes改成shorts
    // Test case 2: write() fails when supplying less data (shorts) than declared.
    @Test
    public void testWriteShortOffsetTooBig() {
        // constants for test
        final String TEST_NAME = "testWriteShortOffsetTooBig";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        short data[] = new short[minBuffSize / 2];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        int offset = 10;

        assertTrue(TEST_NAME, track.write(data, offset, data.length) ==
                AudioTrack.ERROR_BAD_VALUE);

        // -------- tear down --------------
        track.release();
    }

    // Test case 3: write() fails when supplying less data (bytes) than declared.
    @Test
    public void testWriteByteSizeTooBig() {
        // constants for test
        final String TEST_NAME = "testWriteByteSizeTooBig";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.write(data, OFFSET_DEFAULT, data.length + 10)
                == AudioTrack.ERROR_BAD_VALUE);
        // -------- tear down --------------
        track.release();
    }

    // Test case 4: write() fails when supplying less data (shorts) than declared
    @Test
    public void testWriteShortSizeTooBig() {
        // constants for test
        final String TEST_NAME = "testWriteShortSizeTooBig";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        short data[] = new short[minBuffSize / 2];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.write(data, OFFSET_DEFAULT, data.length + 10)
                == AudioTrack.ERROR_BAD_VALUE);
        // -------- tear down --------------
        track.release();
    }

    // Test case 5: write() fails with negative offset.
    @Test
    public void testWriteByteNegativeOffset() {
        // constants for test
        final String TEST_NAME = "testWriteByteNegativeOffset";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];

        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.write(data, OFFSET_NEGATIVE, data.length - 10) ==
                AudioTrack.ERROR_BAD_VALUE);

        // -------- tear down --------------
        track.release();
    }

    // Test case 6: write() fails with negative offset
    @Test
    public void testWriteShortNegativeOffset() throws Exception {
        // constants for test
        final String TEST_NAME = "testWriteShortNegativeOffset";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        short data[] = new short[minBuffSize / 2];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME,
                track.write(data, OFFSET_NEGATIVE, data.length - 10) == AudioTrack.ERROR_BAD_VALUE);
        // -------- tear down --------------
        track.release();
    }

    // Test case 7: write() fails with negative size
    @Test
    public void testWriteByteNegativeSize() throws Exception {
        // constants for test
        final String TEST_NAME = "testWriteByteNegativeSize";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);

        int dataLength = -10;
        assertTrue(TEST_NAME, track.write(data, OFFSET_DEFAULT, dataLength) ==
                AudioTrack.ERROR_BAD_VALUE);

        // -------- tear down --------------
        track.release();
    }

    // Test case 8: write() fails with negative size
    @Test
    public void testWriteShortNegativeSize() throws Exception {
        // constants for test
        final String TEST_NAME = "testWriteShortNegativeSize";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        short data[] = new short[minBuffSize / 2];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        int dataLength = -10;
        assertTrue(TEST_NAME, track.write(data, OFFSET_DEFAULT, dataLength) ==
                AudioTrack.ERROR_BAD_VALUE);

        // -------- tear down --------------
        track.release();
    }

    // Test case 9: write() succeeds and returns the size that was written for 16 bit.
    @Test
    public void testWriteByte() {
        // constants for test
        final String TEST_NAME = "testWriteByte";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.write(data, OFFSET_DEFAULT, data.length) == data.length);
        // -------- tear down --------------
        track.release();
    }

    // Test case 10: write() succeeds and returns the size that was written for 16 bit.
    @Test
    public void testWriteShort() throws Exception {
        // constants for test
        final String TEST_NAME = "testWriteShort";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        short data[] = new short[minBuffSize / 2];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertTrue(TEST_NAME, track.write(data, OFFSET_DEFAULT, data.length) == data.length);
        track.flush();
        // -------- tear down --------------
        track.release();
    }

    // Test case 11: write() succeeds and returns the size that was written for
    // 8bit
    @Test
    public void testWriteByte8bit() throws Exception {
        // constants for test
        final String TEST_NAME = "testWriteByte8bit";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertEquals(TEST_NAME, data.length, track.write(data, OFFSET_DEFAULT, data.length));
        // -------- tear down --------------
        track.release();
    }

    // Test case 12: write() succeeds and returns the size that was written for
    // 8bit
    @Test
    public void testWriteShort8bit() throws Exception {
        // constants for test
        final String TEST_NAME = "testWriteShort8bit";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                2 * minBuffSize, TEST_MODE);
        short data[] = new short[minBuffSize / 2];
        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        assertEquals(TEST_NAME, data.length, track.write(data, OFFSET_DEFAULT, data.length));
        // -------- tear down --------------
        track.release();
    }

    // -----------------------------------------------------------------
    // Getters
    // ----------------------------------

    // 比特率在4000-96000之间

    // Test case 1: getMinBufferSize() return ERROR_BAD_VALUE if SR < 4000
    @Test
    public void testGetMinBufferSizeTooLowSR() {
        // constant for test
        final String TEST_NAME = "testGetMinBufferSizeTooLowSR";
        final int TEST_SR = 3999;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_8BIT;

        assertTrue(TEST_NAME, AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT) ==
                AudioTrack.ERROR_BAD_VALUE);
    }

    // Test case 2: getMinBufferSize() return ERROR_BAD_VALUE if sample rate too high.
    @Test
    public void testGetMinBufferSizeTooHighSR() {
        // constant for test
        final String TEST_NAME = "testGetMinBufferSizeTooHighSR";
        // FIXME need an API to retrieve AudioTrack.SAMPLE_RATE_HZ_MAX
        final int TEST_SR = 96001;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
        // -------- initialization & test --------------
        // 已经不设上限。
        assertTrue(TEST_NAME, AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT) > 0);
    }

    @Test
    public void testAudioTrackProperties() {
        // constants for test
        final String TEST_NAME = "testAudioTrackProperties";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);

        MockAudioTrack track = new MockAudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF,
                TEST_FORMAT, 2 * minBuffSize, TEST_MODE);

        assertEquals(TEST_NAME, AudioTrack.STATE_INITIALIZED, track.getState());
        assertEquals(TEST_NAME, TEST_FORMAT, track.getAudioFormat());
        assertEquals(TEST_NAME, TEST_CONF, track.getChannelConfiguration());
        assertEquals(TEST_NAME, TEST_SR, track.getSampleRate());
        assertEquals(TEST_NAME, TEST_STREAM_TYPE, track.getStreamType());

        final int channelCount = 1;
        assertEquals(channelCount, track.getChannelCount());
        final int notificationMarkerPosition = 0;
        // zero if marker is disabled
        assertEquals(TEST_NAME, notificationMarkerPosition, track.getNotificationMarkerPosition());

        // 作用是什么？
        final int markerInFrames = 2;
        assertEquals(TEST_NAME, AudioTrack.SUCCESS, track.setNotificationMarkerPosition(markerInFrames));
        assertEquals(TEST_NAME, markerInFrames, track.getNotificationMarkerPosition());

        // 周期
        final int positionNotificationPeriod = 0;
        assertEquals(TEST_NAME, positionNotificationPeriod, track.getPositionNotificationPeriod());

        final int periodInFrames = 2;
        assertEquals(TEST_NAME, AudioTrack.SUCCESS,
                track.setPositionNotificationPeriod(periodInFrames));
        assertEquals(TEST_NAME, periodInFrames, track.getPositionNotificationPeriod());

        int frameCount = 2 * minBuffSize;

        if (TEST_CONF == AudioFormat.CHANNEL_OUT_STEREO) {
            frameCount /= 2;
        }
        if (TEST_FORMAT == AudioFormat.ENCODING_PCM_16BIT) {
            frameCount /= 2;
        }
    }

    @Test
    public void testReloadStaticData() throws InterruptedException {
        // constants for test
        final String TEST_NAME = "testReloadStaticData";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        // -------- initialization --------------
        int bufferSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        byte data[] = createSoundDataInByteArray(bufferSize, TEST_SR, 1024);
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                bufferSize, TEST_MODE);
        // -------- test --------------
        track.write(data, OFFSET_DEFAULT, bufferSize);
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        track.play();
        Thread.sleep(WAIT_MSEC);
        track.stop();
        Thread.sleep(WAIT_MSEC);
        assertEquals(TEST_NAME, AudioTrack.SUCCESS, track.reloadStaticData());
        track.play();
        Thread.sleep(WAIT_MSEC);
        track.stop();
        // -------- tear down --------------
        track.release();
    }

    // 声音的频率

    /**
     * 音调（pitch）：声音的高低（高音、低音），
     * 由“频率”（frequency）决定，频率越高音调越高（频率单位Hz（hertz），赫兹[/url，人耳听觉范围20～20000Hz。
     */
    public static byte[] createSoundDataInByteArray(int bufferSamples, final int sampleRate,
                                                    final double frequency) {
        // 采样率是声音频率的两倍。
        final double rad = 2 * Math.PI * frequency / sampleRate;
        byte[] vai = new byte[bufferSamples];
        for (int j = 0; j < vai.length; j++) {
            // 正弦波
            int unsigned = (int) (Math.sin(j * rad) * Byte.MAX_VALUE) + Byte.MAX_VALUE & 0xFF;
            vai[j] = (byte) unsigned;
        }

        return vai;
    }

    public static short[] createSoundDataInShortArray(int bufferSamples, final int sampleRate,
                                                      final double frequency) {
        final double rad = 2 * Math.PI * frequency / sampleRate;
        short[] vai = new short[bufferSamples];
        for (int j = 0; j < vai.length; j++) {
            vai[j] = (short) (Math.sin(j * rad) * Short.MAX_VALUE);
        }
        return vai;
    }

    public static float[] createSoundDataInFloatArray(int bufferSamples, final int sampleRate,
                                                      final double frequency) {
        final double rad = 2 * Math.PI * frequency / sampleRate;
        float[] vaf = new float[bufferSamples];
        for (int j = 0; j < vaf.length; j++) {
            vaf[j] = (float) (Math.sin(j * rad));
        }
        return vaf;
    }

    public static int getBytesPerSample(int audioFormat) {
        switch (audioFormat) {
            case AudioFormat.ENCODING_PCM_8BIT:
                return 1;
            case AudioFormat.ENCODING_PCM_16BIT:
            case AudioFormat.ENCODING_DEFAULT:
                return 2;
            case AudioFormat.ENCODING_PCM_FLOAT:
                return 4;
            case ENCODING_INVALID:
            default:
                throw new IllegalArgumentException("Bad audio format " + audioFormat);
        }
    }

    @Test
    public void testPlayStreamData() throws Exception {
        // constants for test
        final String TEST_NAME = "testPlayStreamData";
        final int TEST_FORMAT_ARRAY[] = {  // should hear 40 increasing frequency tones, 3 times
                AudioFormat.ENCODING_PCM_8BIT,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.ENCODING_PCM_FLOAT,
        };
        final int TEST_SR_ARRAY[] = {
                4000,
                22050,
                44100,
                48000,
                96000,
        };
        final int TEST_CONF_ARRAY[] = {
                AudioFormat.CHANNEL_OUT_MONO,    // 1.0
                AudioFormat.CHANNEL_OUT_STEREO,  // 2.0
                // AudioFormat.CHANNEL_OUT_STEREO | AudioFormat.CHANNEL_OUT_FRONT_CENTER, // 3.0
                // AudioFormat.CHANNEL_OUT_QUAD,    // 4.0
                // AudioFormat.CHANNEL_OUT_QUAD | AudioFormat.CHANNEL_OUT_FRONT_CENTER,   // 5.0
                // AudioFormat.CHANNEL_OUT_5POINT1, // 5.1
                // AudioFormat.CHANNEL_OUT_5POINT1 | AudioFormat.CHANNEL_OUT_BACK_CENTER, // 6.1
                // AudioFormat.CHANNEL_OUT_7POINT1_SURROUND, // 7.1
        };
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;

        for (int TEST_FORMAT : TEST_FORMAT_ARRAY) {
            double frequency = 400;     // frequency changes for each test
            for (int TEST_SR : TEST_SR_ARRAY) {
                for (int TEST_CONF : TEST_CONF_ARRAY) {
                    // -------- initialization --------------
                    final int minBufferSize = AudioTrack.getMinBufferSize(TEST_SR,
                            TEST_CONF, TEST_FORMAT);        // in bytes
                    final int bufferSamples = 12 * minBufferSize
                            / getBytesPerSample(TEST_FORMAT);
                    final int channelCount = Integer.bitCount(TEST_CONF);
                    final double testFrequency = frequency / channelCount;

                    AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR,
                            TEST_CONF, TEST_FORMAT, minBufferSize, TEST_MODE);
                    assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
                    boolean hasPlayed = false;
                    int written = 0;

                    // test
                    switch (TEST_FORMAT) {
                        case AudioFormat.ENCODING_PCM_8BIT: {
                            byte[] data = createSoundDataInByteArray(bufferSamples, TEST_SR,
                                    testFrequency);
                            while (written < data.length) {
                                int ret = track.write(data, written,
                                        Math.min(data.length - written, minBufferSize));
                                assertTrue(TEST_NAME, ret >= 0);
                                written += ret;
                                if (!hasPlayed) {
                                    track.play();
                                    hasPlayed = true;
                                }
                            }
                            break;
                        }
                        case AudioFormat.ENCODING_PCM_16BIT: {
                            short[] data = createSoundDataInShortArray(
                                    bufferSamples, TEST_SR, testFrequency);

                            while (written < data.length) {
                                int ret = track.write(data, written,
                                        Math.min(data.length - written, minBufferSize));
                                assertTrue(TEST_NAME, ret >= 0);
                                written += ret;
                                if (!hasPlayed) {
                                    track.play();;
                                    hasPlayed = true;
                                }
                            }
                            break;
                        }
                        case AudioFormat.ENCODING_PCM_FLOAT: {
                            float data[] = createSoundDataInFloatArray(
                                    bufferSamples, TEST_SR, testFrequency);

                            while (written < data.length) {
                                int ret = track.write(data, written,
                                        Math.min(data.length - written, minBufferSize),
                                        AudioTrack.WRITE_BLOCKING);
                                assertTrue(TEST_NAME, ret >= 0);
                                written += ret;
                                if (!hasPlayed) {
                                    track.play();
                                    hasPlayed = true;
                                }
                            }
                            break;
                        }
                    }
                    Thread.sleep(WAIT_MSEC);
                    track.stop();;
                    Thread.sleep(WAIT_MSEC);
                    track.release();
                    frequency += 70; // increment test tone frequency

                }
            }
        }
    }

    // byte[] 和ByteBuffer的区别
    @Test
    public void testPlayStreamByteBuffer() throws InterruptedException {
        // constants for test
        final String TEST_NAME = "testPlayStreamByteBuffer";
        final int TEST_FORMAT_ARRAY[] = {  // should hear 4 tones played 3 times
                AudioFormat.ENCODING_PCM_8BIT,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.ENCODING_PCM_FLOAT,
        };
        final int TEST_SR_ARRAY[] = {
                48000,
        };
        final int TEST_CONF_ARRAY[] = {
                AudioFormat.CHANNEL_OUT_STEREO,
        };
        final int TEST_WRITE_MODE_ARRAY[] = {
                AudioTrack.WRITE_BLOCKING,
                AudioTrack.WRITE_NON_BLOCKING,
        };
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        for (int TEST_FORMAT : TEST_FORMAT_ARRAY) {
            double frequency = 800; // frequency changes for each test
            for (int TEST_SR : TEST_SR_ARRAY) {
                for (int TEST_CONF : TEST_CONF_ARRAY) {
                    for (int TEST_WRITE_MODE : TEST_WRITE_MODE_ARRAY) {
                        for (int useDirect = 0; useDirect < 2; ++useDirect) {
                            // -------- initialization --------------
                            int minBufferSize = AudioTrack.getMinBufferSize(
                                    TEST_SR, TEST_CONF, TEST_FORMAT);   // in bytes
                            int bufferSize = 12 * minBufferSize;
                            int bufferSamples = bufferSize /
                                    getBytesPerSample(TEST_FORMAT);
                            AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR,
                                    TEST_CONF, TEST_FORMAT, minBufferSize, TEST_MODE);

                            assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);

                            boolean hasPlayed = false;

                            int written = 0;

                            // 速度上的区别
                            // There is no reason to expect direct buffers to be faster for access
                            // inside the jvm. Their advantage comes when you pass them to native
                            // code -- such as, the code behind channels of all kinds.
                            ByteBuffer bb = (useDirect == 1)
                                    ? ByteBuffer.allocateDirect(bufferSize)
                                    : ByteBuffer.allocate(bufferSize);
                            bb.order(java.nio.ByteOrder.nativeOrder());

                            // -------- test --------------
                            switch (TEST_FORMAT) {
                                case AudioFormat.ENCODING_PCM_8BIT: {
                                    byte data[] = createSoundDataInByteArray(
                                            bufferSamples, TEST_SR, frequency);
                                    bb.put(data);
                                    bb.flip();
                                    break;
                                }case AudioFormat.ENCODING_PCM_16BIT: {
                                    short data[] = createSoundDataInShortArray(
                                            bufferSamples, TEST_SR, frequency);

                                    ShortBuffer sb = bb.asShortBuffer();
                                    sb.put(data);
                                    bb.limit(sb.limit() * 2);
                                    break;
                                }case AudioFormat.ENCODING_PCM_FLOAT: {
                                    float data[] = createSoundDataInFloatArray(
                                            bufferSamples, TEST_SR,
                                            frequency);
                                    FloatBuffer fb = bb.asFloatBuffer();
                                    fb.put(data);
                                    bb.limit(fb.limit() * 4);
                                    break;
                                }
                            }

                            while (written < bufferSize) {
                                int ret = track.write(bb, Math.min(bufferSize - written, minBufferSize),
                                        TEST_WRITE_MODE);
                                assertTrue(TEST_NAME, ret >= 0);
                                written += ret;
                                if (!hasPlayed) {
                                    track.play();
                                    hasPlayed = true;
                                }
                            }

                            Thread.sleep(WAIT_MSEC);
                            track.stop();
                            Thread.sleep(WAIT_MSEC);
                            // -------- tear down --------------
                            track.release();
                            frequency += 200; // increment test tone frequency
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testGetTimestamp() {
        // constants for test
        final String TEST_NAME = "testGetTimestamp";
        final int TEST_SR = 22050;
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STREAM;
        final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
        final int TEST_LOOP_CNT = 2;
        // For jitter we allow 30 msec in frames.  This is a large margin.
        // Often this is just 0 or 1 frames, but that can depend on hardware.
        final int TEST_JITTER_FRAMES_ALLOWED = TEST_SR * 30 / 1000;

        // -------- initialization --------------
        // 每帧数据量2x4字节。
        final int bytesPerFrame =
                getBytesPerSample(TEST_FORMAT)
                * channelCountFromOutChannelMask(TEST_CONF);
        final int minBufferSizeInBytes =
                AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);

        final int bufferSizeInBytes = minBufferSizeInBytes * 3;
        byte[] data = new byte[bufferSizeInBytes];
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBufferSizeInBytes, TEST_MODE);

        // -------- test --------------
        assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
        AudioTimestamp timestamp = new AudioTimestamp();
        boolean hasPlayed = false;

        long framesWritten = 0, lastFramesPresented = 0, lastFramesPresentedAt = 0;
        int cumulativeJitterCount = 0;
        float cumulativeJitter = 0;
        float maxJitter = 0;

        for (int i = 0; i < TEST_LOOP_CNT; i++) {
            final long writeTime = System.nanoTime();

            for (int written = 0; written < data.length; ) {
                int ret = track.write(data, written, Math.min(data.length - written, minBufferSizeInBytes));
                assertTrue(TEST_NAME, ret >= 0);
                written += ret;
                if (!hasPlayed) {
                    track.play();
                    hasPlayed = true;
                }
            }

            framesWritten += data.length /bytesPerFrame;

            // track.getTimestamp may return false if there are no physical HAL outputs.
            // This may occur on TV devices without connecting an HDMI monitor.
            // It may also be true immediately after start-up, as the mixing thread could
            // be idle, but since we've already pushed much more than the minimum buffer size,
            // that is unlikely.
            // Nevertheless, we don't want to have unnecessary failures, so we ignore the
            // first iteration if we don't get a timestamp.
            final boolean result = track.getTimestamp(timestamp);
            assertTrue(TEST_NAME, result || i == 0);
            if (!result) {
                continue;
            }

            final long framesPresented = timestamp.framePosition;
            final long framesPresentedAt = timestamp.nanoTime;

            // We read timestamp here to ensure that seen is greater than presented.
            // This is an "on-the-fly" read without pausing because pausing may cause the
            // timestamp to become stale and affect our jitter measurements.
            final int framesSeen = track.getPlaybackHeadPosition();
            assertTrue(TEST_NAME, framesWritten >= framesSeen);
            assertTrue(TEST_NAME, framesSeen >= framesPresented);

            if (i > 1) { // need delta info from previous iteration (skipping first)
                final long deltaFrames = framesPresented - lastFramesPresented;
                final long deltaTime = framesPresentedAt - lastFramesPresentedAt;
                final long NANOSECONDS_PER_SECOND = 1000000000;
                final long expectedFrames = deltaTime * TEST_SR / NANOSECONDS_PER_SECOND;
                final long jitterFrames = Math.abs(deltaFrames - expectedFrames);
                //Log.d(TAG, "framesWritten(" + framesWritten
                //        + ") framesSeen(" + framesSeen
                //        + ") framesPresented(" + framesPresented
                //        + ") jitter(" + jitterFrames + ")");
                // We check that the timestamp position is reasonably accurate.
                assertTrue(TEST_NAME, deltaTime >= 0);
                assertTrue(TEST_NAME, deltaFrames >= 0);
                if (i > 2) {
                    // The first two periods may have inherent jitter as the audio pipe
                    // is filling up. We check jitter only after that.
                    assertTrue(TEST_NAME, jitterFrames < TEST_JITTER_FRAMES_ALLOWED);
                    cumulativeJitter += jitterFrames;
                    cumulativeJitterCount++;
                    if (jitterFrames > maxJitter) {
                        maxJitter = jitterFrames;
                    }
                }
                //Log.d(TAG, "lastFramesPresentedAt(" + lastFramesPresentedAt
                //        + ") writeTime(" + writeTime
                //        + ") framesPresentedAt(" + framesPresentedAt + ")");
                // We check that the timestamp time is reasonably current.
                assertTrue(TEST_NAME, framesPresentedAt >= writeTime);
                assertTrue(TEST_NAME, writeTime >= lastFramesPresentedAt);
            }
            lastFramesPresented = framesPresented;
            lastFramesPresentedAt = framesPresentedAt;
        }
    }

    /**
     * Return the number of channels from an output channel mask.
     */
    public static int channelCountFromOutChannelMask(int mask) {
        return Integer.bitCount(mask);
    }

    // 完全和AudioTrack一样。
    private class MockAudioTrack extends AudioTrack {
        public MockAudioTrack(int streamType, int sampleRateInHz, int channelConfig,
                              int audioFormat, int bufferSizeInBytes, int mode) {
            super(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
        }
    }
}
