package napps.saveanything.Control;

import android.content.Context;

import java.util.LinkedHashSet;
import java.util.Objects;

import napps.saveanything.Utilities.AppLogger;

/**
 * Created by nithesh on 6/16/2016.
 */
/*
    Singleton instance created using double checked locking mechanism. For the reason
    http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples

    Initially this taskManager was made specific for BitmapManager. There was only a cache initialization which
    made this different from generic Taskmanager. So we moved it to corresponding Manager and have made this
    Manager generic

    We have made this abstract so that no one should initialize. They can only extend this class and add tasks to it
    Since this does not much work for executing tasks we should implement retry schedulers and manage
    failed tasks more efficiently

    Initially this implemented runnable so that managing tasks should run on the background. But task managing especially
    the mechanism to wait till the next task becomes available became difficult. So this would be a normal class in UI thread
    managing tasks and submits the task to executorService asap to avoid delays in ui thread.
 */
public abstract class TaskManager<K, V> implements TaskListener<Task> {

    //private static TaskManager sInstance;

    /*
        Retrieve all the failed tasks and try again for executing or after sometime
     */
    private LinkedHashSet<Task> mFailedTasks;

    /*public static TaskManager getInstance(){
        if(sInstance == null){
            synchronized (TaskManager.class){
                if(sInstance == null){
                    sInstance = new TaskManager();
                }
            }
        }

        return sInstance;
    }

    private TaskManager(){

    } */

    private static final String CLASS_TAG = TaskManager.class.getSimpleName();

    private TaskQueue<Task> mQueue;

    private BackgroundWorker mWorker;

    private int mManagerState;

    private Object lockObject;

    private TaskListener<Task> mTaskListener;

    private int STATE_MANAGER_INITIALIZED = 101;
    private int STATE_TASKS_EXECUTING = 102;
    private int STATE_IDLE = 103;

    private Context mContext;

    public void initialize(Context context, int capacity){
        this.mContext = context;
        mQueue = TaskQueue.getInstance();
        mQueue.initializeQueue(capacity);
        mWorker = BackgroundWorker.getInstance();
        mManagerState = STATE_MANAGER_INITIALIZED;
        mTaskListener = this;
        mFailedTasks = new LinkedHashSet<Task>();
        lockObject = new Object();
    }

    /*
        This method is exposed so that it can add tasks
     */
    public void addTask(Task task){
        task.setTaskManager(this);
        mQueue.add(task);
        mTaskListener.onTaskAdded(task);
    }

    /*
        These methods are made final because of the following reasons
        1. These are internal methods used for task execution
        2. A class should not override these methods to create confusion
     */

    private final void executeTasks(){
        mManagerState = STATE_IDLE;
    }

    /*
        The task will post all the actions to TaskManager and taskManager will handle those accordingly
     */
    @Override
    public void  onTaskAdded(Task task) {
        /*
            If the state is idle/initialized currently there are no tasks to run.
            So we have to notify that tasks are available to execute
            If manager is already in executing state no need to call executing
         */
        if(!(mManagerState == STATE_TASKS_EXECUTING)){
            Thread.State state = Thread.currentThread().getState();
            if(state == Thread.State.NEW || state == Thread.State.RUNNABLE){
                run();
            } else if(state == Thread.State.WAITING) {
                notify();
            }
        }
    }

    @Override
    public void onTaskRemoved(Task task) {

    }

    @Override
    public void onTaskFailed(Task task) {
        mFailedTasks.add(task);
        mFailedTasks.remove(task);
    }

    /*
        Initially there was an error-
        This should actually be onTaskCompleted(Task task, V value) but don't know why there is some error
        which is happening
        "Taskcompleted in taskmanager and tasklistener both methods have same erasure but neither overrides the other"
        Need to check this scenario
        So we had to typecast from object to K and V
        Once the task is completed successfully task object is returned but the value can't be passed in as a
        parameter in order to maintain TaskListener interface generic naming standards.
        Anyway there will be an object 'value' created in the heap space. So we will copy the reference to other reference
        and retrieve the value here
     */

    @Override
    public void onTaskCompleted(Task task) {
        /*
            Once the task is complete send the resultant value and along with task Id.
            We would have sent back the task reference itself. If we send that user might hold the reference back avoiding
            garbage collector to free up memory
            So after sending the result we have to remove the task. If we just make it null it won't be removed
            since we will be passing the reference of id and value which will be in task's heap that won't allow
            task to be garbage collected. So we have to copy the contents and make the values null
            TODO: Instead of passing the references of task id and value, pass the copied values
         */

        postResult(task.getTASK_ID(), (V)task.getResultValue());
    }

    public abstract void postResult(long taskId, V value);

    public void shutDown(){
        //TODO: Pass an abstract method to subclass to clear resources
        mWorker.stop();
        //mCache.clear();
        mQueue.clear();
    }

    @Override
    public void run() {

        while(!mQueue.isEmpty()){
            mManagerState = STATE_TASKS_EXECUTING;
            Task task = mQueue.getNext();
            mWorker.addTasktoExecute(task);
            //we have to remove the task as soon as it is added to execute or else incoming tasks
            //try to remove the tasks in the queue which might be currently executing
            mQueue.removeTask(task);

            //When there are no tasks to execute task manager will set its state to idle and goes to wait state
            if(mQueue.isEmpty()){
                mManagerState = STATE_IDLE;
                try{
                    synchronized (this){
                        wait();
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                    AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "run()", "Task Manager interrupted while waiting");
                }
            }
        }

    }
}
