package com.android.mm.music.custom;

public class MusicUtils {

    private static final String TAG = "MusicUtils";

    private final static long MAX_DRM_RING_TONE_SIZE = 300 * 1024; // 300KB



    public interface Defs {
        public final static int OPEN_URL = 0;
        public final static int ADD_TO_PLAYLIST = 1;
        public final static int USE_AS_RINGTONE = 2;
        public final static int PLAYLIST_SELECTED = 3;
        public final static int NEW_PLAYLIST = 4;
        public final static int PLAY_SELECTION = 5;
        public final static int GOTO_START = 6;
        public final static int GOTO_PLAYBACK = 7;
        public final static int PARTY_SHUFFLE = 8;
        public final static int SHUFFLE_ALL = 9;
        public final static int DELETE_ITEM = 10;
        public final static int SCAN_DONE = 11;
        public final static int QUEUE = 12;
        public final static int EFFECTS_PANEL = 13;
        public final static int MORE_MUSIC = 14;
        public final static int MORE_VIDEO = 15;
        public final static int USE_AS_RINGTONE_2 = 16;
        public final static int DRM_LICENSE_INFO = 17;
        public final static int CLOSE = 18;
        public final static int CHILD_MENU_BASE = 19;// this should be the last item;
    }
}
