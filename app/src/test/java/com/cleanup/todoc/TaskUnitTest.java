package com.cleanup.todoc;

import androidx.lifecycle.MutableLiveData;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;
import com.cleanup.todoc.ui.MainViewModel;
import com.cleanup.todoc.ui.MainViewState;
import com.cleanup.todoc.ui.SortMethod;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class TaskUnitTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final TaskRepository mTaskRepository = Mockito.mock(TaskRepository.class);
    private final ProjectRepository mProjectRepository = Mockito.mock(ProjectRepository.class);
    private  ExecutorService mExecutor;

    private final MutableLiveData<List<Task>> tasksMutableLiveData = new MutableLiveData<>();

    private MainViewModel mMainViewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(tasksMutableLiveData).when(mTaskRepository).getTasks();
        mExecutor = Executors.newSingleThreadExecutor();
        mMainViewModel = new MainViewModel(mTaskRepository, mProjectRepository, mExecutor);

    }

    @After
    public void teardown(){ mExecutor.shutdown(); }

    @Test
    public void nominal_case() throws InterruptedException {
        // Given
        tasksMutableLiveData.setValue(getDefaultTasks());

        // When
        List<MainViewState> result = LiveDataTestUtil.getOrAwaitValue(mMainViewModel.getAllTaskLiveData());


        // Then
        assertEquals(getDefaultMainViewState(), result);

    }

    @Test
    public void az_comparator() throws InterruptedException {
        // Given
        tasksMutableLiveData.setValue(getDefaultTasks());

        // When
        mMainViewModel.sortMethod((SortMethod.ALPHABETICAL));
        List<MainViewState> result = LiveDataTestUtil.getOrAwaitValue(mMainViewModel.getAllTaskLiveData());


        // Then
        assertEquals(getDefaultMainViewState(), result);


    }

    @Test
    public void za_comparator() throws InterruptedException {
        // Given
        tasksMutableLiveData.setValue(getDefaultTasks());

        // When
        mMainViewModel.sortMethod((SortMethod.ALPHABETICAL_INVERTED));
        List<MainViewState> result = LiveDataTestUtil.getOrAwaitValue(mMainViewModel.getAllTaskLiveData());


        // Then
        assertEquals(result, getInvertedMainViewState());

    }

    @Test
    public void recent_first_comparator() throws InterruptedException {
        // Given
        tasksMutableLiveData.setValue(getDefaultTasks());

        // When
        mMainViewModel.sortMethod((SortMethod.RECENT_FIRST));
        List<MainViewState> result = LiveDataTestUtil.getOrAwaitValue(mMainViewModel.getAllTaskLiveData());


        // Then
        assertEquals(result, getRecentFirstMainViewState());

    }

    @Test
    public void old_first_comparator() throws InterruptedException {
        // Given
        tasksMutableLiveData.setValue(getDefaultTasks());

        // When
        mMainViewModel.sortMethod((SortMethod.OLD_FIRST));
        List<MainViewState> result = LiveDataTestUtil.getOrAwaitValue(mMainViewModel.getAllTaskLiveData());


        // Then
        assertEquals(result, getOldFirstMainViewState());

    }

    @Test
    public void create_task() throws InterruptedException {
        // When
        mMainViewModel.createTask(new Task (3, "E", 935));


        // Then
        Mockito.verify(mTaskRepository).createTask(new Task (3, "E", 935));


    }

    @Test
    public void delete_task() throws InterruptedException {
        // When
        mMainViewModel.deleteTask(new MainViewState(8, "my first task", 1));



        // Then
        Mockito.verify(mTaskRepository).deleteTask(8);

    }


    private List<Task> getDefaultTasks() {
        return Arrays.asList(
                new Task(0, 1, "A", 123),
                new Task(1,2, "B", 100),
                new Task(2,3, "C", 825),
                new Task(3,4, "D", 756)
        );
    }

    private List<MainViewState> getDefaultMainViewState() {
        return Arrays.asList(
                new MainViewState(0, "A", 1),
                new MainViewState(1, "B", 2),
                new MainViewState(2, "C", 3),
                new MainViewState(3, "D", 4)
        );
    }

    private List<MainViewState> getInvertedMainViewState() {
        return Arrays.asList(
                new MainViewState(3, "D", 4),
                new MainViewState(2, "C", 3),
                new MainViewState(1, "B", 2),
                new MainViewState(0, "A", 1)
        );
    }

    private List<MainViewState> getRecentFirstMainViewState() {
        return Arrays.asList(
                new MainViewState(2, "C", 3),
                new MainViewState(3, "D", 4),
                new MainViewState(0, "A", 1),
                new MainViewState(1, "B", 2)
        );
    }

    private List<MainViewState> getOldFirstMainViewState() {
        return Arrays.asList(
                new MainViewState(1, "B", 2),
                new MainViewState(0, "A", 1),
                new MainViewState(3, "D", 4),
                new MainViewState(2, "C", 3)
        );
    }

    private List<MainViewState> withCreatedMainViewState() {
        return Arrays.asList(
                new MainViewState(1, "B", 2),
                new MainViewState(0, "A", 1),
                new MainViewState(3, "D", 4),
                new MainViewState(2, "C", 3),
                new MainViewState(2, "C", 3)
        );
    }

}