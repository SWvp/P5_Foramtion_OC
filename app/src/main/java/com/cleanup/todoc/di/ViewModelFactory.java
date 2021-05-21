package com.cleanup.todoc.di;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;
import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.ui.MainViewModel;

import java.util.concurrent.Executor;

/**
 * Created by stéphane Warin OCR on 14/05/2021.
 */

public class ViewModelFactory implements ViewModelProvider.Factory{

    private final TaskRepository mTaskRepository;
    private final ProjectRepository mProjectRepository;
    private final Executor mExecutor;


    public ViewModelFactory(TaskRepository taskRepository, ProjectRepository projectRepository, Executor executor) {
        mTaskRepository = taskRepository;
        mProjectRepository = projectRepository;
        mExecutor = executor;

    }

    // Create an instance for each viewModel
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivity.class)) {
            return (T) new MainViewModel(mTaskRepository, mProjectRepository, mExecutor);

        }

        throw new IllegalArgumentException("Unknown ViewModel class");

    }
}