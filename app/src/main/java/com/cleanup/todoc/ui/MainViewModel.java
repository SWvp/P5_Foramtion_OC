package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;


public class MainViewModel extends ViewModel {

    @NonNull
    private final TaskRepository mTaskRepository;
    @NonNull
    private final ProjectRepository mProjectRepository;
    private final Executor mExecutor;
    private Task taskToDelete;

    private long taskIdViewState;
    private List<Task> taskList;

    private final MediatorLiveData<List<MainViewState>> taskListMediatorLiveData = new MediatorLiveData<>();

    private final MutableLiveData<SortMethod> sortedListMutableLiveData = new MutableLiveData<>();


    public MainViewModel(@NonNull TaskRepository taskRepository, @NonNull ProjectRepository projectRepository, Executor executor) {
        mTaskRepository = taskRepository;
        mProjectRepository = projectRepository;
        mExecutor = executor;

        LiveData<List<Task>> taskListLiveData = mTaskRepository.getTasks();

        taskListMediatorLiveData.addSource(taskListLiveData, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> task) {
                combine(task, sortedListMutableLiveData.getValue());

            }
        });

        taskListMediatorLiveData.addSource(sortedListMutableLiveData, new Observer<SortMethod>() {
            @Override
            public void onChanged(SortMethod sortMethod) {
                combine(taskListLiveData.getValue(), sortMethod);

            }
        });
    }

    private void combine(@Nullable List<Task> tasks, @Nullable SortMethod sortMethod) {

        if(sortMethod == null){
            taskListMediatorLiveData.setValue(map(tasks));

        }else {
            switch (sortMethod){

                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case NONE:
                    break;
            }
            taskListMediatorLiveData.setValue(map(tasks));
        }
    }

    private List<MainViewState> map(List<Task> tasks){
        List<MainViewState> result = new ArrayList<>();
        taskList = tasks;

        for (Task task: tasks) {

            result.add(new MainViewState(
                    task.getId(),
                    task.getName(),
                    task.getProjectId()
            ));

        }
        return result;

    }

    public void sortMethod(SortMethod value){
        sortedListMutableLiveData.setValue(value);


    }

    public void createTask(Task task) {
        mExecutor.execute(() -> {
            this.mTaskRepository.createTask(task);
        });
    }

    public void deleteTask(MainViewState task) {
        taskIdViewState = task.getTaskId();
        for (Task mTask: taskList){
            if (mTask.getId() == taskIdViewState){
                taskToDelete = mTask;
            }
        }
        mExecutor.execute(() -> {
            this.mTaskRepository.deleteTask(taskToDelete);
        });
    }

    public LiveData<List<MainViewState>> getAllTaskLiveData() {return taskListMediatorLiveData;}

}
