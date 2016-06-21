package napps.saveanything.Control;

/**
 * Created by nithesh on 6/18/2016.
 */
/*
    Initially this was made specific to handling bitmaps by having key and value pairs.
    THen it was made generic to 'Task's. But in future if someone wants to implement their own tasks
    this should support that. So we have made a generic type 'T' which by nomenclature represents any task
 */
public interface TaskListener<T> {


    void onTaskAdded(T task);

    void onTaskRemoved(T task);

    void onTaskFailed(T task);

    void onTaskCompleted(T task);
}
