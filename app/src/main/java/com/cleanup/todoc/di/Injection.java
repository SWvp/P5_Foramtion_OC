package com.cleanup.todoc.di;

import android.content.Context;

import com.cleanup.todoc.database.TodocDataBase;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by stéphane Warin OCR on 20/05/2021.
 */

public class Injection {
    public static TaskRepository provideTaskDataSource(Context context) {
        TodocDataBase database = TodocDataBase.getInstance(context);
        return new TaskRepository(database.taskDao());
    }

    public static ProjectRepository provideProjectDataSource(Context context) {
        TodocDataBase database = TodocDataBase.getInstance(context);
        return new ProjectRepository(database.projectDao());
    }

    public static Executor provideExecutor() {return Executors.newSingleThreadExecutor();}

    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskRepository dataSourceTask = provideTaskDataSource(context);
        ProjectRepository dataSourceProject = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceTask, dataSourceProject, executor);
    }
}
