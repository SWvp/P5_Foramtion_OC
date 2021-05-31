package com.cleanup.todoc.di;

import android.app.Application;
import android.content.Context;

import com.cleanup.todoc.MainApplication;
import com.cleanup.todoc.database.TodocDataBase;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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

    public static ViewModelFactory provideViewModelFactory() {
        Application application = MainApplication.getApplication();
        TaskRepository dataSourceTask = provideTaskDataSource(application);
        ProjectRepository dataSourceProject = provideProjectDataSource(application);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceTask, dataSourceProject, executor);

    }
}
