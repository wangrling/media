package com.android.mm.libgdx.gdx.audio;


import com.android.mm.libgdx.gdx.Application;
import com.android.mm.libgdx.gdx.ApplicationListener;
import com.android.mm.libgdx.gdx.Audio;
import com.android.mm.libgdx.gdx.files.FileHandle;
import com.android.mm.libgdx.gdx.utils.Disposable;

/**
 * A Music instance represents a streamed audio file. The interface supports pausing,
 * resuming and so on. When you are done with using the Music instance you have to
 * dispose it via the {@link #dispose()} method.
 *
 * Music instances are created via {@link Audio#newMusic(FileHandle)}.
 *
 * Music instances are automatically paused and resume when an {@link Application} is paused
 * or resumed. See {@link ApplicationListener}.
 *
 * Node: any value provided will not be clamped, it is developer's responsibility to do so.
 */

// Music定义接口，子类实现接口。

public interface Music extends Disposable {

    /**
     * Starts the playback of the music stream. In case the stream was paused this will resume the play back.
     * In case the music stream is finished playing this will restart the play back.
     */
    public void play();

    /**
     * Pauses the play back. If the music stream has not been started yet or has finished playing
     * a call to this method will be ignored.
     * 如果音乐没有启动该方法调用无效。
     */
    public void pause();

    /**
     * Stops a playing or paused Music instance. Next time play() is invoked the Music will start
     * from the beginning.
     * 音乐从头开始，seek到0位置。
     */
    public void stop();

    /**
     * @return  whether this music is playing.
     */
    public boolean isPlaying();

    /**
     * Sets whether the music stream is looping. This can be called at any time, whether the stream is playing.
     *
     * @param isLooping whether to loop the stream.
     */
    public void setLooping(boolean isLooping);

    /**
     * @return  whether the music stream is Looping.
     */
    public boolean isLooping();

    /**
     * Set the volume of this music stream. The volume must be given in the range [0, 1] with 0
     * being silent and 1 being the maximum volume.
     *
     * @param volume
     */
    public void setVolume(float volume);

    /**
     * @return  return the volume of this music stream.
     */
    public float getVolume();

    /**
     * Sets the panning and volume of this music stream.
     * 表示声音的位置？
     * @param pan   panning in the range -1 (full left to 1 (full right). 0 is center position.
     * @param volume    the volume in the range [0, 1]
     */
    public void setPan(float pan, float volume);

    /**
     * Seth the playback position in seconds.
     * @param position
     */
    public void setPosition(float position);

    /**
     * Returns the playback position in seconds.
     * @return
     */
    public float getPosition();

    /**
     * Needs to be called when the Music is no longer needed.
     */
    public void dispose();

    /**
     * Register a callback to be invoked when the end of a music stream has been reached during playback.
     *
     * @param listener
     */
    public void setOnCompletionListener(OnCompletionListener listener);

    // 不仅仅针对Android的设计。
    public interface OnCompletionListener {

        public abstract void onCompletion(Music music);
    }
}
