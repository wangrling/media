package com.android.mm.music;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.RelativeLayout;

import com.android.mm.R;

import androidx.annotation.Nullable;

public class MediaPlaybackActivity extends Activity {

    // 专辑
    private Worker mAlbumArtWorker;
    private AlbumArtHandler mAlbumArtHandler;

    private RelativeLayout mAudioPlayerBody;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Looper创建就会运行
        mAlbumArtWorker = new Worker("album art worker");
        mAlbumArtHandler = new AlbumArtHandler(mAlbumArtWorker.getLooper());

        // 监听播放状态的变化
        IntentFilter metaChangeFilter = new IntentFilter();
        metaChangeFilter.addAction(MediaPlaybackService.META_CHANGED);
        metaChangeFilter.addAction(MediaPlaybackService.QUEUE_CHANGED);
        metaChangeFilter.addAction(MediaPlaybackService.PLAYSTATE_CHANGED);
        registerReceiver(mTrackListListener, metaChangeFilter);

        setContentView(R.layout.audio_player_body);
    }

    private BroadcastReceiver mTrackListListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    // 创建一个Looper,然后再创建一个Handler，将Looper作为参数传递给Handler的构造器。
    // 然后创建Handler实例，Handler将会在handleMessage函数上进行相关操作。
    public class AlbumArtHandler extends Handler {

        private long mAlbumId = -1;

        public AlbumArtHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


    private static class Worker implements Runnable {
        private final Object mLock = new Object();
        private Looper mLooper;

        /**
         * Create a worker thread with the given name.
         * The thread then runs a {@link Looper}.
         *
         * @param name A name for the new thread.
         */
        Worker(String name) {
           Thread t = new Thread(null, this, name);
           t.setPriority(Thread.MIN_PRIORITY);
           t.start();
           synchronized (mLock) {
               while (mLooper == null) {
                   try {
                       mLock.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
        }

        public Looper getLooper() {
            return mLooper;
        }

        public void run() {
            synchronized (mLock) {
                Looper.prepare();
                // Return the Looper object associated with the current thread.
                mLooper = Looper.myLooper();
                mLock.notifyAll();
            }
            Looper.loop();
        }

        public void quit() {
            mLooper.quit();
        }
    }

    // 直接在UI主线程运行
    public Handler mLyricHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
