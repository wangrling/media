package com.android.mm.arch.mvp.detail;

import android.os.Bundle;

import com.android.mm.R;
import com.android.mm.arch.mvp.Injection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_detail_act);

        setSupportActionBar(findViewById(R.id.toolbar));

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested task id.
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.add(R.id.contentFrame, taskDetailFragment);
            transaction.commit();
        }

        // Create the presenter
        // 显示具体的任务
        new TaskDetailPresenter(
                taskId, Injection.provideTasksRepository(getApplicationContext()),
                taskDetailFragment);
    }
}
