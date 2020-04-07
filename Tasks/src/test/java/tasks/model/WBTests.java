package tasks.model;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import tasks.repository.AbstractTaskRepository;
import tasks.repository.LinkedTaskRepository;
import tasks.services.TasksService;
import tasks.view.Main;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;


public class WBTests {

    @Mock
    private static AbstractTaskRepository repository;

    private static ClassLoader classLoader;
    private static File savedTasksFile;

    private static TasksService service;

    @BeforeAll
    static void beforeAll() {
        classLoader = Main.class.getClassLoader();
        savedTasksFile = new File(classLoader.getResource("data/tasks.txt").getFile());
        service = new TasksService(savedTasksFile);


    }

    @AfterAll
    static void afterAll() {
    }

    @BeforeEach
    void beforeEach() {
        repository = new LinkedTaskRepository();
    }

    @Test()
    @DisplayName("TC01_WB_noTaskInIntervalFilter")
    void TC01_WB_noTaskInIntervalFilter() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date startTaskDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date endTaskDate = calendar.getTime();

        Date start = Date.from(
                LocalDate
                        .of(2020, 5, 11)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );
        Date end = Date.from(
                LocalDate
                        .of(2020, 6, 11)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );

        Task t1 = new Task("t0", startTaskDate, endTaskDate, 200);
        t1.setActive(true);
        repository.add(t1);
        TaskIO.writeBinary(repository, savedTasksFile);
        Iterable<Task> tasks = service.filterTasks(start, end);
        assert (((Collection<Task>) tasks).size() == 0);

    }

    @Test()
    @DisplayName("TC02_WB_taskStartBeforeIntervalTest")
    void TC02_WB_taskStartBeforeIntervalTest() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date startTaskDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date endTaskDate = calendar.getTime();

        Date start = Date.from(
                LocalDate
                        .of(2020, 2, 11)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );
        Date end = Date.from(
                LocalDate
                        .of(2020, 6, 11)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );


        Task t1 = new Task("t0", startTaskDate, endTaskDate, 200);
        t1.setActive(true);
        repository.add(t1);
        TaskIO.writeBinary(repository, savedTasksFile);
        Iterable<Task> tasks = service.filterTasks(start, end);
        assert (((Collection<Task>) tasks).size() == 1);

    }

    @Test()
    @DisplayName("TC03_WB_startTaskEqualStartIntervalTime")
    void TC03_WB_startTaskEqualStartIntervalTime() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date startTaskDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date endTaskDate = calendar.getTime();

        Date end = Date.from(
                LocalDate
                        .of(2020, 6, 11)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );


        Task t1 = new Task("t0", startTaskDate, endTaskDate, 200);
        t1.setActive(true);
        repository.add(t1);
        TaskIO.writeBinary(repository, savedTasksFile);
        Iterable<Task> tasks = service.filterTasks(startTaskDate, end);
        assert (((Collection<Task>) tasks).size() == 1);

    }


    @Test()
    @DisplayName("TC04_WB_startIntervalTimeBetweenStartAndEndTaskTime")
    void TC04_WB_startIntervalTimeBetweenStartAndEndTaskTime() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date startTaskDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date endTaskDate = calendar.getTime();

        Date start = Date.from(
                LocalDate
                        .of(2020, 4, 9)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );
        Date end = Date.from(
                LocalDate
                        .of(2020, 6, 11)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );


        Task t1 = new Task("t0", startTaskDate, endTaskDate, 3);
        t1.setActive(true);
        repository.add(t1);
        TaskIO.writeBinary(repository, savedTasksFile);
        Iterable<Task> tasks = service.filterTasks(start, end);
        assert (((Collection<Task>) tasks).size() == 1);

    }

    @Test()
    @DisplayName("TC05_WB_activeTaskButNotRepeated")
    void TC05_WB_activeTaskButNotRepeated() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date startTaskDate = calendar.getTime();
        Date start = Date.from(
                LocalDate
                        .of(2020, 2, 5)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );
        Date end = Date.from(
                LocalDate
                        .of(2020, 6, 11)
                        .atStartOfDay(
                                ZoneId.of("Europe/Bucharest")
                        )
                        .toInstant()
        );


        Task t1 = new Task("t0", startTaskDate);
        t1.setActive(true);
        repository.add(t1);
        TaskIO.writeBinary(repository, savedTasksFile);
        Iterable<Task> tasks = service.filterTasks(start, end);
        assert (((Collection<Task>) tasks).size() == 1);
    }

}
