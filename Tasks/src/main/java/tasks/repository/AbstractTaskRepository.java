package tasks.repository;

import tasks.model.Task;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTaskRepository implements Iterable<Task>, Serializable  {
    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract int size();
    public abstract Task getTask(int index);
    public abstract List<Task> getAll();

    public abstract Iterator<Task> iterator();

    public AbstractTaskRepository incoming(Date from, Date to){
        AbstractTaskRepository incomingTasks;
        if (this instanceof ArrayTaskRepository){
            incomingTasks = new ArrayTaskRepository();
        }
        else {
            incomingTasks = new LinkedTaskRepository();
        }

        for(int i = 0; i < this.size(); i++){
            if(getTask(i).nextTimeAfter(from) != null && getTask(i).nextTimeAfter(from).before(to)){
                incomingTasks.add(getTask(i));
                System.out.println(getTask(i).getTitle());
            }
        }
        return incomingTasks;
    }



}