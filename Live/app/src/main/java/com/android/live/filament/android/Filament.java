package com.android.live.filament.android;

public class Filament {

    static {
        // 获取当前Platform和加载本地库。
        Platform.get();
        System.loadLibrary("filament");
    }

    private Filament() {

    }

    @SuppressWarnings("unused")
    public static void init() {

    }
}
