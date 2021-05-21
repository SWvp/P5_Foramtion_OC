package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;


@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocDataBase extends RoomDatabase {

    // Singleton, allow to get only one reference instance, to avoid conflict
    private static volatile TodocDataBase INSTANCE;

    // Get all dao
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    // Instance
    public static TodocDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDataBase.class, "TodocDatabase")
                            .addCallback(prepopulateDatabase())
                            .build();

                }
            }
        }
        return INSTANCE;

    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Project[] projects = Project.getAllProjects();

                for (Project project : projects) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", project.getName());
                    contentValues.put("color", project.getColor());
                    db.insert("projects", OnConflictStrategy.IGNORE, contentValues);

                }
            }
        };
    }
}
