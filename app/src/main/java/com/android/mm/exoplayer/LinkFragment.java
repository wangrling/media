package com.android.mm.exoplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LinkFragment extends Fragment {

    private final String[] DEMOS = {
            "Simple player (最简单的播放器)"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(android.R.layout.simple_list_item_1, container);

        view.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, DEMOS));
        return view;
    }
}
