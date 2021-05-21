package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class MainViewModel extends ViewModel {

    @NonNull
    private final TaskRepository mTaskRepository;
    @NonNull
    private final ProjectRepository mProjectRepository;
    private final Executor mExecutor;

    private final MediatorLiveData<List<Task>> taskListMediatorLiveData = new MediatorLiveData<>();


    public MainViewModel(TaskRepository taskRepository, ProjectRepository projectRepository, Executor executor) {
        mTaskRepository = taskRepository;
        mProjectRepository = projectRepository;
        mExecutor = executor;

        LiveData<List<Task>> taskListLiveData = mTaskRepository.getTasks();

        taskListMediatorLiveData.addSource(taskListLiveData, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> task) {

            }
        });
    }

    private void combine(@Nullable List<Task> tasks) {
        List<Task> filteredTasks = new ArrayList<>();

        taskListMediatorLiveData.setValue(filteredTasks);

    }




    public void createTask(Task task) {
        mExecutor.execute(() -> {
            this.mTaskRepository.createTask(task);
        });
    }

    public void deleteTask(Task task) {
        mExecutor.execute(() -> {
            this.mTaskRepository.deleteTask(task);
        });
    }



    public LiveData<List<Task>> getAllTask() {return taskListMediatorLiveData;}


}
