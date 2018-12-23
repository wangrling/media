package com.android.mm.libgdx.gdx;

import com.android.mm.libgdx.gdx.audio.AudioDevice;
import com.android.mm.libgdx.gdx.audio.AudioRecorder;
import com.android.mm.libgdx.gdx.audio.Music;
import com.android.mm.libgdx.gdx.audio.Sound;
import com.android.mm.libgdx.gdx.files.FileHandle;

/**
 * This interface encapsulates the creation and management of audio resources. It allows you to get
 * direct access to the audio hardware via the {@link AudioDevice} and {@link AudioRecorder}
 * interfaces, create sound effects via the {@link Sound} interface and play music streams via
 * the {@link Music} interface.
 *
 * All resource create via this interface have to be disposed as soon as they are no longer used.
 *
 * Note that all {@link Music} instances will be automatically paused when the
 * {@link ApplicationListener#pause()} method is called, and automatically resumed when the
 * {@link ApplicationListener#resume()} method is called.
 */

public interface Audio {

    /**
     * Creates a new {@link AudioDevice} either in mono or stereo mode. The AudioDevice has to be
     * disposed via its {@link AudioDevice#dispose()} method when it is no longer used.
     *
     * @param samplingRate
     * @param isMono    When the AudioDevice should be in mono or stereo mode.
     * @return  the AudioDevice
     * @throws RuntimeException in case the device could not be created.
     */
    public AudioDevice newAudioDevice(int samplingRate, boolean isMono);

    /**
     * Create a new {@link AudioRecorder}. The AudioRecorder has to be disposed after it is no
     * longer used.
     *
     * @param sampleRate    the sampling rate in Hertz.
     * @param isMono        the recorder records in mono or stereo
     * @return
     * @throws RuntimeException in case the recorder could not be created.
     */
    public AudioRecorder newAudioRecorder(int sampleRate, boolean isMono);

    /**
     * Creates a new {@link Sound} which is used to play back audio effects such as gun shots
     * or explosions. The Sound's audio data is retrieved from the file specified via the
     * {@link FileHandle}. Note that the complete audio data is loaded into RAM. YOu
     * should therefore not load big audio files with this methods. The current upper limit
     * for decoded audio is 1 MB.
     *
     * Currently supported formats are WAV, MP3 and OGG.
     *
     * The sound has to be disposed if it is no longer used via the {@link Sound@dispose()} method.
     *
     * @return the new sound
     * @throws RuntimeException in case the sound could not be loaded.
     */
    public Sound newSound(FileHandle fileHandle);


    /**
     * Create a new {@link Music} instance which is used to play back a music stream from
     * a file. Currently supported formats are WAV, MP3 and OGG. The music instance has to
     * be disposed if it is no longer used via the {@link Music#dispose()} method.
     * Music instances are automatically paused when {@link ApplicationListener#pause()} is
     * called and resume when {@link ApplicationListener#resume()} is called.
     *
     * @param file  the FileHandle
     * @return  the new Music or null if the Music could not be loaded
     * // GdxRuntimeException完全是RuntimeException的封装。
     * @throws RuntimeException in case the music could be loaded.
     */
    public Music newMusic(FileHandle file);
}
