package com.android.mm.music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;

public class MediaPlaybackService extends Service {


    static class ServiceStub extends IMediaPlaybackService.Stub {
        WeakReference<MediaPlaybackService> mService;

        ServiceStub(MediaPlaybackService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void openFile(String path) throws RemoteException {

        }

        @Override
        public void open(long[] list, int position) throws RemoteException {

        }

        @Override
        public int getQueuePosition() throws RemoteException {
            return 0;
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public void stop() throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {

        }

        @Override
        public void play() throws RemoteException {

        }

        @Override
        public void prev() throws RemoteException {

        }

        @Override
        public void next() throws RemoteException {

        }

        @Override
        public long duration() throws RemoteException {
            return 0;
        }

        @Override
        public long position() throws RemoteException {
            return 0;
        }

        @Override
        public long seek(long pos) throws RemoteException {
            return 0;
        }

        @Override
        public String getTrackName() throws RemoteException {
            return null;
        }

        @Override
        public String getAlbumName() throws RemoteException {
            return null;
        }

        @Override
        public long getAlbumId() throws RemoteException {
            return 0;
        }

        @Override
        public String getArtistName() throws RemoteException {
            return null;
        }

        @Override
        public long getArtistId() throws RemoteException {
            return 0;
        }

        @Override
        public void enqueue(long[] list, int action) throws RemoteException {

        }

        @Override
        public long[] getQueue() throws RemoteException {
            return new long[0];
        }

        @Override
        public void moveQueueItem(int from, int to) throws RemoteException {

        }

        @Override
        public void setQueuePosition(int index) throws RemoteException {

        }

        @Override
        public String getPath() throws RemoteException {
            return null;
        }

        @Override
        public long getAudioId() throws RemoteException {
            return 0;
        }

        @Override
        public void setShuffleMode(int shuffleMode) throws RemoteException {

        }

        @Override
        public int getShuffleMode() throws RemoteException {
            return 0;
        }

        @Override
        public int removeTracks(int first, int last) throws RemoteException {
            return 0;
        }

        @Override
        public int removeTrack(long id) throws RemoteException {
            return 0;
        }

        @Override
        public void setRepeatMode(int repeatMode) throws RemoteException {

        }

        @Override
        public int getRepeatMode() throws RemoteException {
            return 0;
        }

        @Override
        public int getMediaMountedCount() throws RemoteException {
            return 0;
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return 0;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return mBinder;
    }

    private final IBinder mBinder = new ServiceStub(this);
}
