package com.cleanup.todoc;

import androidx.lifecycle.MutableLiveData;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;
import com.cleanup.todoc.ui.MainViewModel;
import com.cleanup.todoc.ui.MainViewState;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;

public class TaskUnitTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final TaskRepository mTaskRepository = Mockito.mock(TaskRepository.class);
    private final ProjectRepository mProjectRepository = Mockito.mock(ProjectRepository.class);
    private final Executor mExecutor = Mockito.mock(Executor.class);

    private final MutableLiveData<List<Task>> tasksMutableLiveData = new MutableLiveData<>();

    private MainViewModel mMainViewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(tasksMutableLiveData).when(mTaskRepository).getTasks();
        mMainViewModel = new MainViewModel(mTaskRepository, mProjectRepository, mExecutor);

    }

    @Test
    public void nominal_case() throws InterruptedException {
        // Given
        tasksMutableLiveData.setValue(getDefaultTasks());

        // When
        List<MainViewState> result = LiveDataTestUtil.getOrAwaitValue(mMainViewModel.getAllTaskLiveData());


        // Then
        assertEquals(result, getDefaultMainViewState());

    }

    @Test
    public void az_comparator() throws InterruptedException {
        // Given


        // When



        // Then


    }

    @Test
    public void za_comparator() throws InterruptedException {
        // Given


        // When



        // Then


    }

    @Test
    public void recent_comparator() throws InterruptedException {
        // Given


        // When



        // Then


    }

    @Test
    public void old_comparator() throws InterruptedException {
        // Given


        // When



        // Then


    }

    @Test
    public void create_task() throws InterruptedException {
        // Given


        // When



        // Then


    }

    @Test
    public void delete_task() throws InterruptedException {
        // When
        mMainViewModel.deleteTask(new MainViewState(1, "my first task", 1));



        // Then
        Mockito.verify(mTaskRepository).deleteTask(new Task( 1, "my first task", 123));

    }


    private List<Task> getDefaultTasks() {
        return Arrays.asList(
                new Task( 1, "my first task", 123)
                //new Task(2, 2, "my second task", 256),
                //new Task(3, 3, "my third task", 825),
                //new Task(4, 1, "my fourth task", 956)
        );
    }

    private List<MainViewState> getDefaultMainViewState() {
        return Arrays.asList(
                new MainViewState(1, "my first task", 1)
                //new MainViewState(2, "my second task", 2),
                //new MainViewState(3, "my third task", 3),
                //new MainViewState(4, "my fourth task", 1)
        );
    }

}