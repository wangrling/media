package com.android.mm.algorithms;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

public class AlgorithmsActivity extends ListActivity {

    private static final List<String> algorithms = Arrays.asList(
        "Bag: collection which does not allow removing elements (only collect and iterate)."
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                algorithms));
    }
}
