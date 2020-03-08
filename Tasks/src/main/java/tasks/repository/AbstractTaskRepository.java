package tasks.repository;

import org.apache.log4j.Logger;
import tasks.model.Task;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class AbstractTaskRepository implements Iterable<Task>, Serializable  {

    private static final Logger log = Logger.getLogger(AbstractTaskRepository.class.getName());

    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract int size();
    public abstract Task getTask(int index);
    public abstract List<Task> getAll();

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
                log.info(getTask(i).getTitle());
            }
        }
        return incomingTasks;
    }



}