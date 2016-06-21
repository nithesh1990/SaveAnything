package napps.saveanything.Control;

import napps.saveanything.Utilities.AppLogger;

/**
 * Created by nithesh on 6/16/2016.
 */
/*
    Initially this task was made to only execute bitmap decoding tasks. Since we have amy tasks
    that should be run efficiently in the background this task is made more general so that Save image tasks,
    bitmap tasks, network tasks are all executed using this tasks
 */
public abstract class Task<K, V> implements Runnable{

    public static final int STORAGE_PRIORITY_TOP = 0;
    public static final int STORAGE_PRIORITY_MEDIUM = 1;
    public static final int STORAGE_PRIORITY_BOTTOM = 2;

    private int mPriority;

    private long TASK_ID;

    private V resultValue;

    private ResponseListener<K, V> responseListener;

    /*
        Every Task should have reference of TaskManager to notify the actions of Task
     */
    private TaskManager<K, V> mManager;

    /*
        These methods are made final because of the following reasons
        1. These are internal methods used for task execution
        2. A class should not override these methods to create confusion
        All methods which shouldn't be interrupted by user should be made final
     */

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
        //Before executing we will check if there is a task Id set or else we will set the task id by ourselves
        if(!(getTASK_ID() > 0)){
            //We wil use the time in millis as that is the easily available unique identifier
            setTASK_ID(System.currentTimeMillis());
        }
        V value = execute();
        setResultValue(value);
        if(value != null){
            mManager.onTaskCompleted(this);
        } else {
            mManager.onTaskFailed(this);
        }
    }

    public final TaskManager<K, V> getTaskManager() {
        return mManager;
    }

    public final void setTaskManager(TaskManager<K, V> mManager) {
        this.mManager = mManager;
    }

    public final long getTASK_ID() {
        return TASK_ID;
    }

    public final void setTASK_ID(long TASK_ID) {
        this.TASK_ID = TASK_ID;
    }

    public V getResultValue() {
        return resultValue;
    }

    /*
        This is made final and private because the result value should only be set inside and neither should
        be executed nor it should be set externally
     */
    private final void setResultValue(V resultValue) {
        this.resultValue = resultValue;
    }
}
