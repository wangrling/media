package com.android.live.filament.jni;

public class Filament {

    static {
        Platform.get();
        System.loadLibrary("filament");
    }

    private Filament() {

    }

    @SuppressWarnings("unused")
    public static void init() {

    }
}
