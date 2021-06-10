package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;


public class TaskRepository {

    private final TaskDao mTaskDao;

    public TaskRepository(TaskDao taskDao) { mTaskDao = taskDao; }

    // Get all tasks
    public LiveData<List<Task>> getTasks() {
        return this.mTaskDao.getTasks();
    }

    // Create
    public void createTask(Task task) {
        mTaskDao.insertTask(task);
    }

    // Delete
    public void deleteTask(long taskId) {
        mTaskDao.deleteTask(taskId);
    }

}
