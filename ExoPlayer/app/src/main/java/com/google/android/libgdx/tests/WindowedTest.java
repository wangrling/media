package com.google.android.libgdx.tests;

import android.content.Intent;
import android.opengl.GLES30;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;

import com.google.android.libgdx.ApplicationListener;
import com.google.android.libgdx.android.AndroidApplication;

public class WindowedTest extends AndroidApplication implements ApplicationListener {

    public void onCreate (Bundle bundle) {
        super.onCreate(bundle);

        Button b1 = new Button(this);
        b1.setText("change color");
        b1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Button b2 = new Button(this);
        b2.setText("new window");
        b2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        View view = initializeForView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(b1);
        layout.addView(b2);
        layout.addView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setContentView(layout);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View arg0) {

            }

        });

        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                Intent intent = new Intent(WindowedTest.this, WindowedTest.class);
                WindowedTest.this.startActivity(intent);
            }
        });
    }

    public void onPause () {
        super.onPause();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        Log.w("WindowedTest", "destroying");
    }

    @Override
    public void create () {
    }

    @Override
    public void render () {
        // Gdx.gl.glClearColor(color.r, color.g, color.g, color.a);
        // Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        GLES30.glClearColor(0.3f, 0.4f, 0.5f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

    }

    @Override
    public void dispose () {
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }
}
