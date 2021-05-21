package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

/**
 * Created by stéphane Warin OCR on 20/05/2021.
 */

public class ProjectRepository {

    private final ProjectDao mProjectDao;

    public ProjectRepository(ProjectDao projectDao) { mProjectDao = projectDao; }

    public LiveData<List<Project>> getAllProjects() {return this.mProjectDao.getAllProjects();}

}
