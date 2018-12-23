package com.android.mm.libgdx.gdx.audio;

import com.android.mm.libgdx.gdx.utils.Disposable;

/**
 * Encapsulates an audio device in mono or stereo mode. Use the
 * {@link #writeSamples(float[], int, int)} and {@link #writeSamples(short[], int, int)}
 * methods to write float or 16-bit signed short PCM data directly to the audio
 * device.
 * Stereo samples are interleaved in the order left channel sample, right channel
 * sample. The {@link #dispose()} method msut be called when this AudioDevice is no
 * long needed.
 */

public interface AudioDevice extends Disposable {

    /**
     * @return  whether this AudioDevice is in mono or stereo mode.
     */
    public boolean isMono();

    /**
     * Writes the array of 16-bit signed PCM samples to the audio device
     * and blocks until they have been processed.
     *
     * @param samples   The samples
     * @param offset        The offset into the samples array
     * @param numSamples    the number of samples to write to the device.
     */
    public void writeSamples(short[] samples, int offset, int numSamples);

    /**
     * Writes the array of float PCM samples to the audio device and blocks until
     * they have been processed.
     *
     * @param samples   The samples.
     * @param offset    The offset into the samples array.
     * @param numSamples    the number of samples to write to the device
     */
    public void writeSamples(float[] samples, int offset, int numSamples);

    /**
     * @return  the latency in samples.
     */
    public int getLatency();

    /**
     * Frees all resources associated with this AudioDevice. Need to be called when the
     * device is no longer needed.
     */
    public void dispose();

    /**
     * Sets the volume in the range [0, 1].
     */
    public void setVolume(float volume);
}
