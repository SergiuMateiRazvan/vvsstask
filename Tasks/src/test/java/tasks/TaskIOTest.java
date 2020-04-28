package tasks;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import tasks.model.Task;
import tasks.model.TaskIO;
import tasks.repository.AbstractTaskRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
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

    private static Date someFutureDate;

    @BeforeAll
    static void beforeAll() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        someFutureDate = calendar.getTime();
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
    @DisplayName("TC01_EC")
    void writeOutputStreamErrorTest() {
        out = null;
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(new Task("t0", new Date()));
        assertThrows(NullPointerException.class, () -> TaskIO.write(repository, out));
    }

    @Test()
    @DisplayName("TC02_EC")
    void tasksError() {
        repository = null;
        assertThrows(NullPointerException.class, () -> TaskIO.write(repository, out));
    }

    @Test()
    @DisplayName("TC03_EC")
    void writeSome() throws Exception {
        when(tasksIterator.hasNext()).thenReturn(true, true, true, false);
        Task[] tasks = new Task[3];
        tasks[0] = new Task("t0", someFutureDate);
        tasks[1] = new Task("t1", someFutureDate);
        tasks[2] = new Task("t2", someFutureDate);
        when(tasksIterator.next()).thenReturn(tasks[0], Arrays.copyOfRange(tasks, 1, tasks.length));
        TaskIO.write(repository, out);
        verify(tasksIterator, times(3)).next();
    }

    @Test()
    @DisplayName("TC04_EC")
    void tasksHasNoEntries() throws Exception {
        when(repository.size()).thenReturn(0);
        when(tasksIterator.hasNext()).thenReturn(false);

        TaskIO.write(repository, out);

        verify(tasksIterator, times(0)).next();
        verify(tasksIterator, times(1)).hasNext();
    }

    @Test()
    @DisplayName("TC03_BVA")
    void taskTitleEmptyString() {
        Task task = new Task("", mock(Date.class));
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        assertThrows(RuntimeException.class, () -> TaskIO.write(repository, out));
    }

    @DisplayName("TC04_BVA")
    @ParameterizedTest
    @ValueSource(strings = { "A", "B", "C" })
    void taskTitleLengthIs1(String title) throws IOException {
        Task task = new Task(title, mock(Date.class));
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        TaskIO.write(repository, out);
        verify(tasksIterator, times(1)).next();
    }

    @ParameterizedTest
    @ValueSource(strings = { "1", "2", "3" })
    void test_ValueSource_String(String s) {
        assertTrue(Integer.parseInt(s) < 5);
    }

    @Test()
    @DisplayName("TC04_BVA")
    void taskTitleLengthIs30() throws IOException {
        Task task = new Task(new String("mySizeIs30mySizeIs30mySizeIs30"), mock(Date.class));
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        TaskIO.write(repository, out);
        verify(tasksIterator, times(1)).next();
    }

    @Test()
    @DisplayName("TC05_BVA")
    void taskTitleLengthIs29() throws IOException {
        Task task = new Task(new String("my size is 29!!my size is 29!!"), mock(Date.class));
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        TaskIO.write(repository, out);
        verify(tasksIterator, times(1)).next();
    }

    @Test()
    @DisplayName("TC07_BVA")
    void taskTitleLengthIs31() throws IOException {
        Task task = new Task("mySizeIs31mySizeIs31mySizeIs31!", mock(Date.class));
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        assertThrows(RuntimeException.class, () -> TaskIO.write(repository, out));
    }

    @DisplayName("TC08_BVA")
    @RepeatedTest(3)
    void dateIsTheCurrentDate() throws IOException {
        Task task = new Task("task", new Date());
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        TaskIO.write(repository, out);
        verify(tasksIterator, times(1)).next();
    }

    @Test
    @DisplayName("TC09_BVA")
    void dateIsOneDayBeforeTheCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        Task task = new Task("task", yesterday);
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        assertThrows(RuntimeException.class, () -> TaskIO.write(repository, out));
    }

    @Test
    @DisplayName("TC09_BVA")
    void dateIsOneDayAfterTheCurrentDate() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date yesterday = calendar.getTime();
        Task task = new Task("task", yesterday);
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        TaskIO.write(repository, out);
        verify(tasksIterator, times(1)).next();
    }

    @Test
    @DisplayName("TC10_BVA")
    void dateIsOneYearAndOneDayAfterTheCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);
        Date yesterday = calendar.getTime();
        Task task = new Task("task", yesterday);
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        assertThrows(RuntimeException.class, () -> TaskIO.write(repository, out));
    }

    @Test
    @DisplayName("TC11_BVA")
    void dateIsOneYearAfterTheCurrentDate() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date yesterday = calendar.getTime();
        Task task = new Task("task", yesterday);
        when(repository.size()).thenReturn(1);
        when(tasksIterator.hasNext()).thenReturn(true, false);
        when(tasksIterator.next()).thenReturn(task);

        TaskIO.write(repository, out);
        verify(tasksIterator, times(1)).next();
    }
}