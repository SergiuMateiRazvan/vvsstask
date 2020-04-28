package tasks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tasks.model.Task;
import tasks.model.TaskIO;
import tasks.repository.AbstractTaskRepository;
import tasks.repository.ArrayTaskRepository;
import tasks.view.Main;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TaskIOFullIntegrationTest {

    @Mock
    private static OutputStream out;
    @Mock
    private static AbstractTaskRepository repository;
    private static ClassLoader classLoader;

    private static Date someFutureDate;
    private static Task t;

    @BeforeAll
    static void beforeAll() {
        classLoader = Main.class.getClassLoader();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        someFutureDate = calendar.getTime();
        t = new Task("Title", new Date());
        writeToFile();
    }

    @BeforeEach
    void beforeEach() {
        repository = new ArrayTaskRepository();
    }


    @AfterEach
    void afterEach() {
        repository = null;
    }

    @Test
    void write() {
        out = mock(OutputStream.class);
        repository.add(t);
        assertDoesNotThrow(() -> TaskIO.write(repository, out));
    }

    @Test
    void read() throws IOException {
        FileInputStream fis = new FileInputStream(new File(classLoader.getResource("data/tasks_stub.txt").getFile()));
        TaskIO.read(repository, fis);
        assertEquals(1, repository.size());
    }


    static void writeToFile() {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File(classLoader.getResource("data/tasks_stub.txt").getFile())))) {
            dataOutputStream.writeInt(1);
            dataOutputStream.writeInt(t.getTitle().length());
            dataOutputStream.writeUTF(t.getTitle());
            dataOutputStream.writeBoolean(t.isActive());
            dataOutputStream.writeInt(t.getRepeatInterval());
            if (t.isRepeated()){
                dataOutputStream.writeLong(t.getStartTime().getTime());
                dataOutputStream.writeLong(t.getEndTime().getTime());
            }
            else {
                dataOutputStream.writeLong(t.getTime().getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}