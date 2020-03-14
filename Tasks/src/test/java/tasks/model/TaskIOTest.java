package tasks.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


import org.mockito.Mock;

import tasks.repository.AbstractTaskRepository;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import static org.mockito.Mockito.*;


class TaskIOTest {

    @Mock
    private static OutputStream out;
    @Mock
    private static AbstractTaskRepository repository;

    @Mock
    private Iterator<Task> tasksIterator = mock(Iterator.class);

    @BeforeAll
    static void beforeAll() {
    }

    @AfterAll
    static void afterAll() {
        out = null;
        repository = null;
    }

    @BeforeEach
    void beforeEach() {
        out = mock(OutputStream.class);
        repository = mock(AbstractTaskRepository.class);
        when(repository.iterator()).thenReturn(tasksIterator);
    }


    @Test()
    void writeOutputStreamErrorTest() {
        out = null;
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(new Task("t0", new Date()));
        assertThrows(NullPointerException.class, () -> TaskIO.write(repository, out));
    }

    @Test()
    void tasksError() {
        repository = null;
        assertThrows(NullPointerException.class, () -> TaskIO.write(repository, out));
    }

    @Test()
    void writeSome() throws Exception {
        when(tasksIterator.hasNext()).thenReturn(true, true, true, false);
        Task[] tasks = new Task[3];
        tasks[0] = new Task("t0", new Date());
        tasks[1] = new Task("t1", new Date());
        tasks[2] = new Task("t2", new Date());
        when(tasksIterator.next()).thenReturn(tasks[0], Arrays.copyOfRange(tasks, 1, tasks.length));
        TaskIO.write(repository, out);
        verify(tasksIterator, times(3)).next();
    }

    @Test()
    void tasksHasNoEntries() throws Exception {
        when(repository.size()).thenReturn(0);
        when(tasksIterator.hasNext()).thenReturn(false);

        TaskIO.write(repository, out);

        verify(tasksIterator, times(0)).next();
        verify(tasksIterator, times(1)).hasNext();
    }
}