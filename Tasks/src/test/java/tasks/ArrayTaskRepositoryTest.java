package tasks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import tasks.model.Task;
import tasks.repository.ArrayTaskRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ArrayTaskRepositoryTest {
    @Mock
    private Task task;
    @Spy
    private List<Task> list;
    @Spy
    private ArrayTaskRepository repo, other_repo;

    @BeforeEach
    void beforeEach(){
        task = mock(Task.class);
        list = new ArrayList<>();
        repo = new ArrayTaskRepository();
        other_repo = new ArrayTaskRepository();
    }

    @AfterEach
    void afterEach(){
        task = null;
        list = null;
        repo = null;
        other_repo = null;
    }

    @Test
    void add() {
        int size = repo.size();
        repo.add(task);
        assertEquals(size + 1, repo.size());
    }

    @Test
    void remove() {
        int size = repo.size();
        repo.add(task);
        repo.remove(task);
        assertEquals(size, repo.size());
    }

    @Test
    void getAll(){
        list.add(task);
        repo.add(task);
        assertEquals(list, repo.getAll());
    }

    @Test
    void equals(){
        other_repo.add(task);
        repo.add(task);
        assertEquals(repo, other_repo);
        other_repo.remove(task);
        assertNotEquals(repo, other_repo);
    }
}