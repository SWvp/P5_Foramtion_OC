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

    private final MediatorLiveData<List<MainViewState>> taskListMediatorLiveData = new MediatorLiveData<>();

    private final MutableLiveData<SortMethod> azMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<SortMethod> zaMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<SortMethod> oldestFirstMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<SortMethod> recentFirstMutableLiveData = new MutableLiveData<>();


    public MainViewModel(@NonNull TaskRepository taskRepository, @NonNull ProjectRepository projectRepository, Executor executor) {
        mTaskRepository = taskRepository;
        mProjectRepository = projectRepository;
        mExecutor = executor;

        LiveData<List<Task>> taskListLiveData = mTaskRepository.getTasks();

        taskListMediatorLiveData.addSource(taskListLiveData, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> task) {
                combine(task, azMutableLiveData.getValue(), zaMutableLiveData.getValue(), oldestFirstMutableLiveData.getValue(), recentFirstMutableLiveData.getValue());

            }
        });

        taskListMediatorLiveData.addSource(azMutableLiveData, new Observer<SortMethod>() {
            @Override
            public void onChanged(SortMethod az) {
                combine(taskListLiveData.getValue(), az, zaMutableLiveData.getValue(), oldestFirstMutableLiveData.getValue(), recentFirstMutableLiveData.getValue());

            }
        });

        taskListMediatorLiveData.addSource(zaMutableLiveData, new Observer<SortMethod>() {
            @Override
            public void onChanged(SortMethod za) {
                combine(taskListLiveData.getValue(), azMutableLiveData.getValue(), za, oldestFirstMutableLiveData.getValue(), recentFirstMutableLiveData.getValue());

            }
        });

        taskListMediatorLiveData.addSource(oldestFirstMutableLiveData, new Observer<SortMethod>() {
            @Override
            public void onChanged(SortMethod old) {
                combine(taskListLiveData.getValue(), azMutableLiveData.getValue(), zaMutableLiveData.getValue(), old, recentFirstMutableLiveData.getValue());

            }
        });

        taskListMediatorLiveData.addSource(recentFirstMutableLiveData, new Observer<SortMethod>() {
            @Override
            public void onChanged(SortMethod recent) {
                combine(taskListLiveData.getValue(), azMutableLiveData.getValue(), zaMutableLiveData.getValue(), oldestFirstMutableLiveData.getValue(), recent);

            }
        });


    }

    private void combine(@Nullable List<Task> tasks, @Nullable SortMethod az, @Nullable SortMethod za, @Nullable SortMethod old, @Nullable SortMethod recent) {

        if((az == null) && (za == null) && (old == null) && (recent == null)){
            taskListMediatorLiveData.setValue(map(tasks));

        }

        else if(az != null){
            Collections.sort(tasks, new Task.TaskAZComparator());
            taskListMediatorLiveData.setValue(map(tasks));

        }
        else if(za != null){
            Collections.sort(tasks, new Task.TaskZAComparator());
            taskListMediatorLiveData.setValue(map(tasks));

        }
        else if(old != null){
            Collections.sort(tasks, new Task.TaskOldComparator());
            taskListMediatorLiveData.setValue(map(tasks));

        }
        else if(recent != null){
            Collections.sort(tasks, new Task.TaskRecentComparator());
            taskListMediatorLiveData.setValue(map(tasks));

        }
    }

    private List<MainViewState> map(List<Task> tasks){
        List<MainViewState> result = new ArrayList<>();

        for (Task task: tasks) {

            result.add(new MainViewState(
                    task.getId(),
                    task.getName(),
                    task.getProjectId()
            ));

        }
        return result;

    }

    public void azSort(SortMethod value){
        azMutableLiveData.setValue(value);
        zaMutableLiveData.setValue(null);
        oldestFirstMutableLiveData.setValue(null);
        recentFirstMutableLiveData.setValue(null);


    }
    public void zaSort(SortMethod value) {
        azMutableLiveData.setValue(null);
        zaMutableLiveData.setValue(value);
        oldestFirstMutableLiveData.setValue(null);
        recentFirstMutableLiveData.setValue(null);



    }

    public void oldFirstSort(SortMethod value) {
        azMutableLiveData.setValue(null);
        zaMutableLiveData.setValue(null);
        oldestFirstMutableLiveData.setValue(value);
        recentFirstMutableLiveData.setValue(null);

    }

    public void recentFirstSort(SortMethod value) {
        azMutableLiveData.setValue(null);
        zaMutableLiveData.setValue(null);
        oldestFirstMutableLiveData.setValue(null);
        recentFirstMutableLiveData.setValue(value);

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

    public LiveData<List<MainViewState>> getAllTask() {return taskListMediatorLiveData;}

}
