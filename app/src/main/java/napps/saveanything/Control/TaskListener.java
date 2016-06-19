package napps.saveanything.Control;

/**
 * Created by nithesh on 6/18/2016.
 */
public interface TaskListener<K, V> {


    void onTaskAdded(Task task);

    void onTaskRemoved(Task task);

    void onTaskFailed(Task task);

    void onTaskCompleted(K key, V value);
}
