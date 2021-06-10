package com.cleanup.todoc.ui;

import com.cleanup.todoc.model.Project;

import java.util.Objects;


public class MainViewState {
    private final long taskId;
    private final long projectId;
    private final String taskName;

    public MainViewState(long taskId, String taskName, long projectId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.projectId = projectId;
    }

    public long getTaskId(){ return taskId; }


    public String getName() { return taskName;
    }

    public Project getProject() {
        return Project.getProjectById(projectId);
    }

    // For Unit Tests
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainViewState that = (MainViewState) o;
        return taskId == that.taskId &&
                projectId == that.projectId &&
                Objects.equals(taskName, that.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, projectId, taskName);
    }

    @Override
    public String toString() {
        return "MainViewState{" +
                "taskId=" + taskId +
                ", projectId=" + projectId +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
