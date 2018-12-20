package com.android.mm.exoplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.android.mm.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebViewFragment extends Fragment {

    WebView mExoPlayerViewView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mExoPlayerViewView = new WebView(getContext());
        mExoPlayerViewView.loadUrl("file:///android_asset/html/exoplayer.html");

        return mExoPlayerViewView;
    }
}
