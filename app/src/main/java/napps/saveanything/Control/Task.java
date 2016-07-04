package napps.saveanything.Control;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import napps.saveanything.Utilities.AppLogger;

/**
 * Created by nithesh on 6/16/2016.
 */
/*
    Initially this task was made to only execute bitmap decoding tasks. Since we have amy tasks
    that should be run efficiently in the background this task is made more general so that Save image tasks,
    bitmap tasks, network tasks are all executed using this tasks

    Initially it implemented a runnable interface which was fine. But managing tasks was difficult in TaskManager.
    So this was changed to implement so that we get a future object from the executor service to manage tasks
    We can also use runnable for this purpose but callable is used because of following reasons
            Runnable has been around since Java 1.0, but Callable was only introduced in Java 1.5 ... to handle use-cases that Runnable does not support.
            In theory, the Java team could have changed the signature of the Runnable.run() method, but this would have broken binary compatiblity with pre-1.5 code, requiring recoding when migrating old Java code to newer JVMs.
            That is a BIG NO-NO. Java strives to be backwards compatible ... and that's been one of Java's biggest selling points for business computing.

 */
public abstract class Task<K, V> implements Callable<V> {

    public static final int STORAGE_PRIORITY_TOP = 0;
    public static final int STORAGE_PRIORITY_MEDIUM = 1;
    public static final int STORAGE_PRIORITY_BOTTOM = 2;

    private int mPriority;

    private long TASK_ID;

    private Future<V> taskFuturereference;

    private V resultValue;

    private ResponseListener<K, V> responseListener;

    /*
        Every Task should have reference of TaskManager to notify the actions of Task
     */
    private TaskManager<K, V>  mManager;

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

    @Override
    public V call() throws Exception {
        V value = execute();
        setResultValue(value);
        if(value != null){
            mManager.onTaskCompleted(this);
        } else {
            mManager.onTaskFailed(this);
        }

        return value;
    }

    public Future<V> getTaskFuturereference() {
        return taskFuturereference;
    }

    public void setTaskFuturereference(Future<V> taskFuturereference) {
        this.taskFuturereference = taskFuturereference;
    }
}
