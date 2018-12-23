package com.android.mm.ndk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mm.R;

import androidx.annotation.Nullable;

public class GlmTestActivity extends Activity {

    static {
        System.loadLibrary("glmtest");
    }

    private static final String[] TESTS = {
            /**
             *  Core features
             * 	Features that implement in C++ the GLSL specification as closely as possible.
             */
            // test core_fun_common.cpp
            "Common functions",
            // test core_fun_exponential.cpp
            "Exponential functions",
            // test core_fun_geometric.cpp
            "Geometric functions",
            // test core_fun_vector_relational.cpp
            "Vector types",
            "Vector types with precision qualifiers",
            "Matrix types",
            "Matrix types with precision qualifiers",
            "Integer functions",
            "Matrix functions",
            "Floating-Point Pack and Unpack Functions",
            "Angle and Trigonometry Functions",
            "Vector Relational Functions",

            /**
             * Stable extensions
             */
            "GLM_EXT_matrix_clip_space",
            "GLM_EXT_matrix_projection",
            "GLM_EXT_matrix_relational",
            "GLM_EXT_matrix_transform",
            "GLM_EXT_quaternion_common",
            "GLM_EXT_quaternion_double",
            "GLM_EXT_quaternion_double_precision",
            "GLM_EXT_quaternion_exponential",
            "GLM_EXT_quaternion_float",

        "mat2x2", "mat4x4", "vec4",
            "constexpr", "geometric", "matrix", "noise",
            "swizzle", "trigonometric",
            "clip_space", "projection", "transform",
            "quaternion", "scalar", "epsilon", "round",
            "euler_angle", "gradient_paint", "ortho_normalize",
            "perpendicular", "polar_coordinate", "random",
            "rotate_normalized", "simd", "spline",
            "texture", "pref"

    };

    // 成功的测试数。
    int failCount;

    ListView mListView;

    Button mTestButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view_test);

        mListView = findViewById(R.id.list);

        mListView.setAdapter(new ArrayAdapter<>(this, R.layout.wrapper_simple_expandable_list_item,
                TESTS));

        mTestButton = findViewById(R.id.test);

        ((TextView)(findViewById(R.id.total))).setText("Total     : " + TESTS.length);
      ((TextView)(findViewById(R.id.success))).setText("Success : ");
        ((ImageView)findViewById(R.id.fail)).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mTestButton.setOnClickListener((v) -> {
            failCount = checkTests();
            updateUIResult();
        });
    }

    private static native int checkTests();


    private void updateUIResult() {
        if (failCount == 0) {
            findViewById(R.id.check).setVisibility(View.VISIBLE);
            findViewById(R.id.fail).setVisibility(View.GONE);
        } else {
            findViewById(R.id.fail).setVisibility(View.VISIBLE);
            findViewById(R.id.check).setVisibility(View.GONE);
        }
        ((TextView)findViewById(R.id.success)).setText("Success : " + (TESTS.length - failCount));
    }

}
