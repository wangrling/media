package com.android.mm.libgdx.gdx;

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
}
