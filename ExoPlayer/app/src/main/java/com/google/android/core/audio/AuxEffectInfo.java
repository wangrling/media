package com.google.android.core.audio;

import android.media.AudioTrack;
import android.media.audiofx.AudioEffect;

/**
 * Represents auxiliary effect information, which can be used to attach an auxiliary
 * effect to an underlying {@link AudioTrack}.
 *
 * <p>Auxiliary effects can only be applied if the application has the
 * {@code android.permission.MODIFY_AUDIO_SETTINGS} permission. Apps are responsible
 * for retaining the associated audio effect instance and releasing it when it's no
 * long needed. See the documentation of {@link AudioEffect} for more information.
 */

public final class AuxEffectInfo {


}
