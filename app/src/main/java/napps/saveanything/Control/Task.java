package napps.saveanything.Control;

import napps.saveanything.Utilities.AppLogger;

/**
 * Created by nithesh on 6/16/2016.
 */
public abstract class Task<K, V> implements Runnable{

    public static final int STORAGE_PRIORITY_TOP = 0;
    public static final int STORAGE_PRIORITY_MEDIUM = 1;
    public static final int STORAGE_PRIORITY_BOTTOM = 2;

    private int mPriority;

    private K key;


    private ResponseListener<K, V> responseListener;

    /*
        Every Task should have reference of TaskManager to notify the actions of Task
     */
    private TaskManager<K, V> mManager;

    /*
        These methods are made final because of the following reasons
        1. These are internal methods used for task execution
        2. A class should not override these methods to create confusion
     */
    public final K getKey() {
        return key;
    }

    public final void setKey(K key) {
        this.key = key;
    }

    public final int getmPriority() {
        return mPriority;
    }

    public final void setmPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public final ResponseListener<K, V> getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(ResponseListener<K, V> mResponseListener) {
        this.responseListener = mResponseListener;
    }

    public abstract V execute();
    @Override
    public void run() {
        /*
            We will retrieve the value from the task and tell taskManager that task is complete
         */
        V value = execute();
        if(value != null){
            mManager.onTaskCompleted(key, value);
        } else {
            mManager.onTaskFailed(this);
        }
    }

    public final TaskManager<K, V> getTaskManager() {
        return mManager;
    }

    public void setTaskManager(TaskManager<K, V> mManager) {
        this.mManager = mManager;
    }
}
