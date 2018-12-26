package com.android.mm.libgdx;

import android.app.Activity;
import android.os.Bundle;

import com.android.mm.libgdx.android.AndroidAudio;
import com.android.mm.libgdx.gdx.Audio;
import com.android.mm.libgdx.gdx.audio.Music;
import com.android.mm.libgdx.gdx.files.FileHandle;

import androidx.annotation.Nullable;

/**
 * 需要测试音乐播放和提示音播放是否正常，一个使用MediaPlayer播放，一个使用SoundPool播放。
 * 需要测试是否跟随Activity的回调自动进行音乐的播放和暂停操作。
 */

public class AudioTestActivity extends Activity {

    Music music;

    FileHandle fileHandle;

    Audio audio = new AndroidAudio(this, null);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        music = audio.newMusic(fileHandle);
    }
}
