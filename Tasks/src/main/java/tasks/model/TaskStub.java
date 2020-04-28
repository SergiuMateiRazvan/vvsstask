package tasks.model;
import java.util.Date;

public class TaskStub extends Task {

    private Date stubbedTime = new Date();


    public TaskStub(String title, Date time) {
        super(title, time);
    }

    @Override
    public String getTitle() {
        return "title string";
    }

    @Override
    public Date getTime() {
        return stubbedTime;
    }

    @Override
    public Date getStartTime() {
        return stubbedTime;
    }

    @Override
    public Date getEndTime() {
        return stubbedTime;
    }

    @Override
    public int getRepeatInterval() {
        return 0;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public Date getStubbedTime() {
        return stubbedTime;
    }

    public void setStubbedTime(Date stubbedTime) {
        this.stubbedTime = stubbedTime;
    }
}
