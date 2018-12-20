package com.android.mm.arch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.mm.arch.mvp.tasks.TasksActivity;

import androidx.annotation.Nullable;

public class ArchActivity extends ListActivity {

    private static final String[] ARCH_TESTS = {
            // 三个Presenter, 在add(添加任务), detail(显示任务细节), tasks(任务列表)文件夹里。
            // Presenter是数据和试图的结合点。
            "Model View Presenter"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ARCH_TESTS));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0: {
                startActivity(new Intent(this, TasksActivity.class));
                break;
            }
        }
    }
}
