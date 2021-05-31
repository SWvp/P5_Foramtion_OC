package com.cleanup.todoc.ui;

import com.cleanup.todoc.model.Project;


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
}
