package tasks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import tasks.model.Task;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Mock
    private Date startDate, endDate, time;
    @Spy
    private Task task;
    private Integer interval;
    private String title;

    @BeforeEach
    void beforeEach(){
        startDate = mock(Date.class);
        endDate = mock(Date.class);
        title = "Title";
        interval = 1;
        task = new Task(title, startDate, endDate, interval);
    }

    @AfterEach
    void afterEach(){
        startDate = null;
        endDate = null;
        title = null;
        interval = null;
        task = null;
    }

    @Test
    void setActive() {
        task.setActive(true);
        assertTrue(task.isActive());
    }

    @Test
    void isRepeated() {
        assertTrue(task.isRepeated());
    }

    @Test
    void getStartTime() {
        assertEquals(startDate, task.getStartTime());
    }

    @Test
    void getEndTime() {
        assertEquals(endDate, task.getEndTime());
    }

    @Test
    void getTitle() {
        assertEquals(title, task.getTitle());
    }

    @Test
    void setTime() {
        task.setTime(time);
        assertEquals(time, task.getTime());
    }

    @Test
    void getRepeatInterval() {
        assertEquals(interval, task.getRepeatInterval());
    }

    @Test
    void isRepeatedAndActive() {
        task.setActive(true);
        assertTrue(task.isRepeatedAndActive());
        task.setActive(false);
        assertFalse(task.isRepeatedAndActive());
    }
}