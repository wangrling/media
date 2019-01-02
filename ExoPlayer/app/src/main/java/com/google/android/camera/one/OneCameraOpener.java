package com.google.android.camera.one;

import android.os.Handler;

import com.google.android.camera.FatalErrorHandler;
import com.google.android.camera.SoundPlayer;
import com.google.android.camera.async.MainThread;
import com.google.android.camera.burst.BurstFacade;
import com.google.android.camera.device.CameraId;
import com.google.android.camera.one.v2.photo.ImageRotationCalculator;

/**
 * The camera manager is responsible for instanting {@link OneCamera}
 * instances.
 */
public interface OneCameraOpener {



    public abstract void open(
            CameraId cameraId,
            OneCameraCaptureSetting captureSetting,
            Handler handler,
            MainThread mainThread,
            ImageRotationCalculator imageRotationCalculator,
            BurstFacade burstController,
            SoundPlayer soundPlayer,
            OneCamera.OpenCallback openCallback,
            FatalErrorHandler fatalErrorHandler);
}
