package com.android.mm.arch.mvp;

import android.content.Context;

import com.android.mm.arch.mvp.data.FakeTasksRemoteDataSource;
import com.android.mm.arch.mvp.data.TasksDataSource;
import com.android.mm.arch.mvp.data.TasksLocalDataSource;
import com.android.mm.arch.mvp.data.TasksRepository;
import com.android.mm.arch.mvp.data.ToDoDatabase;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link TasksDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static TasksRepository provideTasksRepository(Context context) {
        checkNotNull(context);
        ToDoDatabase database = ToDoDatabase.getInstance(context);
        // 仓库包含远程数据和本地数据。
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(),
                        database.taskDao()));
    }
}
