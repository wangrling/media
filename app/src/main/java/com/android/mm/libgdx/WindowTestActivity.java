package com.android.mm.libgdx;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.mm.libgdx.android.AndroidApplication;
import com.android.mm.libgdx.gdx.ApplicationListener;
import com.android.mm.libgdx.gdx.graphics.Color;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;

//游戏绘制的回调。
// 在AndroidApplication有ApplicationListener变量，把WindowTestActivity复制给它。
// 关键：View view = initializeForView(this);

/**
 * 需要测试内容
 * 测试这几个回调是否起作用，特别是render函数，它是View渲染的核心，绘制2D/3D界面。
 */

public class WindowTestActivity extends AndroidApplication implements ApplicationListener {

    Color color = new Color(1, 1, 1, 1);

    @Override
    public void create() {
        Button button = new Button(this);
        button.setText("Change Color");
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        View view = initializeForView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(button);
        layout.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        setContentView(layout);

        button.setOnClickListener((v) -> {
            color.set((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
        });

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(color.r, color.g, color.g, color.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
