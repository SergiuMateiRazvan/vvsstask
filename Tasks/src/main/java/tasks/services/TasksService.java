package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.model.TaskIO;
import tasks.repository.ArrayTaskRepository;
import tasks.model.Task;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class TasksService {

    private File savedTasksFile;
    private static final Logger log = Logger.getLogger(TasksService.class.getName());

    public TasksService(File tasksFile) {
        this.savedTasksFile = tasksFile;
    }

    public ObservableList<Task> getObservableList() {
        ArrayTaskRepository tasks = new ArrayTaskRepository();
        try {
            TaskIO.readBinary(tasks, savedTasksFile);
        } catch (IOException e) {
            log.error("File does not exist!"); //should only happen when file is deleted
            // while the app is running
        }
        return FXCollections.observableArrayList(tasks.getAll());
    }
    public String getIntervalInHours(Task task){
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }
    public String formTimeUnit(int timeUnit){
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }
    public int parseFromStringToSeconds(String stringTime){//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        return (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
    }

    public Iterable<Task> filterTasks(Date start, Date end){
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        return tasksOps.incoming(start,end);
    }
}
