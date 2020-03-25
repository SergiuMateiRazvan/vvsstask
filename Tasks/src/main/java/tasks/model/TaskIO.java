package tasks.model;


import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.repository.AbstractTaskRepository;
import tasks.repository.LinkedTaskRepository;
import tasks.view.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskIO {
    private static final String[] TIME_ENTITY = {" day"," hour", " minute"," second"};
    private static final int SECONDS_IN_DAY = 86400;
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MIN = 60;
    private static final Logger log = Logger.getLogger(TaskIO.class.getName());
    private static final String IO_ERROR_MESSAGE = "IO exception reading or writing file";

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");

    private TaskIO() {
    }

    public static void write(AbstractTaskRepository tasks, OutputStream out) throws IOException {
        try(DataOutputStream dataOutputStream = new DataOutputStream(out)) {
            dataOutputStream.writeInt(tasks.size());
            for (Task t : tasks){
                validateTask(t);
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
            }
        }
    }

    private static void validateTask(Task t) {
        // Title
        if(t.getTitle().isEmpty()) throw new RuntimeException("Title cannot be empty");
        if(t.getTitle().length() > 30) throw new RuntimeException("Title cannot have a size larger that 30");

        // Date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        if (t.getTime().before(calendar.getTime())) throw new RuntimeException("You cannot add tasks for a past date");
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.HOUR, 1);
        if (t.getTime().after(calendar.getTime()))
            throw new RuntimeException("You cannot add tasks for a period greater than one year");
    }

    public static void read(AbstractTaskRepository tasks, InputStream in)throws IOException {
        try(DataInputStream dataInputStream = new DataInputStream(in)) {
            int listLength = dataInputStream.readInt();
            for (int i = 0; i < listLength; i++){
                dataInputStream.readInt();
                String title = dataInputStream.readUTF();
                boolean isActive = dataInputStream.readBoolean();
                int interval = dataInputStream.readInt();
                Date startTime = new Date(dataInputStream.readLong());
                Task taskToAdd;
                if (interval > 0){
                    Date endTime = new Date(dataInputStream.readLong());
                    taskToAdd = new Task(title, startTime, endTime, interval);
                }
                else {
                    taskToAdd = new Task(title, startTime);
                }
                taskToAdd.setActive(isActive);
                tasks.add(taskToAdd);
            }
        }
    }
    public static void writeBinary(AbstractTaskRepository tasks, File file)throws IOException{
        try(FileOutputStream fos = new FileOutputStream(file)) {
            write(tasks,fos);
        }
        catch (IOException e){
            log.error(IO_ERROR_MESSAGE);
        }
    }

    public static void readBinary(AbstractTaskRepository tasks, File file) throws IOException{
        try(FileInputStream fis = new FileInputStream(file)) {
            read(tasks, fis);
        }
        catch (IOException e){
            log.error(IO_ERROR_MESSAGE);
        }
    }
    public static void write(AbstractTaskRepository tasks, Writer out) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(out);
        Task lastTask = tasks.getTask(tasks.size()-1);
        for (Task t : tasks){
            bufferedWriter.write(getFormattedTask(t));
            bufferedWriter.write(t.equals(lastTask) ? ';' : '.');
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

    }

    public static void read(AbstractTaskRepository tasks, Reader in)  throws IOException {
        BufferedReader reader = new BufferedReader(in);
        String line;
        Task t;
        while ((line = reader.readLine()) != null){
            t = getTaskFromString(line);
            tasks.add(t);
        }
        reader.close();

    }

    public static void writeText(AbstractTaskRepository tasks, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            write(tasks, fileWriter);
        } catch (IOException e) {
            log.error(IO_ERROR_MESSAGE);
        }
    }

    public static void readText(AbstractTaskRepository tasks, File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            read(tasks, fileReader);
        }
    }

    //// service methods for reading
    private static Task getTaskFromString (String line){
        boolean isRepeated = line.contains("from");//if contains - means repeated
        boolean isActive = !line.contains("inactive");//if doesnt have inactive - means active
        //Task(String title, Date time)   Task(String title, Date start, Date end, int interval)
        Task result;
        String title = getTitleFromText(line);
        if (isRepeated){
            Date startTime = getDateFromText(line, true);
            Date endTime = getDateFromText(line, false);
            int interval = getIntervalFromText(line);
            result = new Task(title, startTime, endTime, interval);
        }
        else {
            Date startTime = getDateFromText(line, true);
            result = new Task(title, startTime);
        }
        result.setActive(isActive);
        return result;
    }

    private static int[] getTimeEntities(String line) {
        int start = line.lastIndexOf('[');
        int end = line.lastIndexOf(']');
        String trimmed = line.substring(start+1, end);//returns interval without brackets -> 2 hours 46 minutes
        int days = trimmed.contains("day") ? 1 : 0;
        int hours = trimmed.contains("hour") ? 1 : 0;
        int minutes = trimmed.contains("minute") ? 1 : 0;
        int seconds = trimmed.contains("second") ? 1 : 0;

        int[] timeEntities = new int[]{days, hours, minutes, seconds};

        int i = 0;
        int j = timeEntities.length-1;// positions of timeEntities available
        while (i != 1 && j != 1) {
            if (timeEntities[i] == 0) i++;
            if (timeEntities[j] == 0) j--;
        }

        String[] numAndTextValues = trimmed.split(" ");
        for (int k = 0 ; k < numAndTextValues.length; k+=2){
            timeEntities[i] = Integer.parseInt(numAndTextValues[k]);
            i++;
        }

       return timeEntities;
    }

    private static int getIntervalFromText(String line){
        int[] timeEntities = getTimeEntities(line);


        int result = 0;
        for (int p = 0; p < timeEntities.length; p++) {
            if (timeEntities[p] != 0) {
                switch (p) {
                    case 0:
                        result += SECONDS_IN_DAY * timeEntities[p];
                        break;
                    case 1:
                        result += SECONDS_IN_HOUR * timeEntities[p];
                        break;
                    case 2:
                        result += SECONDS_IN_MIN * timeEntities[p];
                        break;
                    case 3:
                        result += timeEntities[p];
                        break;
                    default:
                        break;
                }
            }
        }
        return result;
    }

    private static Date getDateFromText (String line, boolean isStartTime) {
        Date date = null;
        String trimmedDate; //date trimmed from whole string
        int start;
        int end;

        if (isStartTime){
            start = line.indexOf('[');
            end = line.indexOf(']');
        }
        else {
            int firstRightBracket = line.indexOf(']');
            start = line.indexOf('[', firstRightBracket+1);
            end = line.indexOf(']', firstRightBracket+1);
        }
        trimmedDate = line.substring(start, end+1);
        try {
            date = simpleDateFormat.parse(trimmedDate);
        }
        catch (ParseException e){
            log.error("date parse exception");
        }
        return date;

    }
    private static String getTitleFromText(String line){
        int start = 1;
        int end = line.lastIndexOf('\"');
        String result = line.substring(start, end);
        result = result.replace("\"\"", "\"");
        return result;
    }


    ////service methods for writing
    private static String getFormattedTask(Task task){
        StringBuilder result = new StringBuilder();
        String title = task.getTitle();
        if (title.contains("\"")) title = title.replace("\"","\"\"");
        result.append("\"").append(title).append("\"");

        if (task.isRepeated()){
            result.append(" from ");
            result.append(simpleDateFormat.format(task.getStartTime()));
            result.append(" to ");
            result.append(simpleDateFormat.format(task.getEndTime()));
            result.append(" every ").append("[");
            result.append(getFormattedInterval(task.getRepeatInterval()));
            result.append("]");
        }
        else {
            result.append(" at ");
            result.append(simpleDateFormat.format(task.getStartTime()));
        }
        if (!task.isActive()) result.append(" inactive");
        return result.toString().trim();
    }

    public static String getFormattedInterval(int interval){
        if (interval <= 0) throw new IllegalArgumentException("Interval <= 0");
        StringBuilder sb = new StringBuilder();

        int days = interval/SECONDS_IN_DAY;
        int hours = (interval - SECONDS_IN_DAY*days) / SECONDS_IN_HOUR;
        int minutes = (interval - (SECONDS_IN_DAY*days + SECONDS_IN_HOUR*hours)) / SECONDS_IN_MIN;
        int seconds = (interval - (SECONDS_IN_DAY*days + SECONDS_IN_HOUR*hours + SECONDS_IN_MIN*minutes));

        int[] time = new int[]{days, hours, minutes, seconds};
        int i = 0;
        int j = time.length-1;
        while (time[i] == 0 || time[j] == 0){
            if (time[i] == 0) i++;
            if (time[j] == 0) j--;
        }

        for (int k = i; k <= j; k++){
            sb.append(time[k]);
            sb.append(time[k] > 1 ? TIME_ENTITY[k]+ "s" : TIME_ENTITY[k]);
            sb.append(" ");
        }
        return sb.toString();
    }


    public static void rewriteFile(ObservableList<Task> tasksList) {
        LinkedTaskRepository taskList = new LinkedTaskRepository();
        for (Task t : tasksList){
            taskList.add(t);
        }
        try {
            TaskIO.writeBinary(taskList, Main.savedTasksFile);
        }
        catch (IOException e){
            log.error(IO_ERROR_MESSAGE);
        }
    }
}
