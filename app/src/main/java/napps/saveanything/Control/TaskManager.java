package napps.saveanything.Control;

import android.content.Context;

import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * Created by nithesh on 6/16/2016.
 */
/*
    Singleton instance created using double checked locking mechanism. For the reason
    http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples

 */
public abstract class TaskManager<K, V> implements TaskListener {

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

    private QueueHashCache<K, V> mCache;

    private TaskQueue<Task> mQueue;

    private BackgroundWorker mWorker;

    private int mManagerState;

    private TaskListener mTaskListener;

    private int STATE_MANAGER_INITIALIZED = 101;
    private int STATE_TASKS_EXECUTING = 102;
    private int STATE_IDLE = 103;

    private Context mContext;

    public void initialize(Context context, int capacity){
        this.mContext = context;
        mCache = QueueHashCache.getsInstance();
        mCache.initialize(capacity);
        mQueue = TaskQueue.getInstance();
        mQueue.initializeQueue(capacity);
        mWorker = BackgroundWorker.getInstance();
        mManagerState = STATE_MANAGER_INITIALIZED;
        mTaskListener = this;
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
        while(!mQueue.isEmpty()){
            mManagerState = STATE_TASKS_EXECUTING;
            Task task = mQueue.getNext();
            mWorker.addTasktoExecute(task);
            //we have to remove the task as soon as it is added to execute or else incoming tasks
            //try to remove the tasks in the queue which might be currently executing
            mQueue.removeTask(task);
        }
        mManagerState = STATE_IDLE;
    }

    /*
        The task will post all the actions to TaskManager and taskManager will handle those accordingly
     */
    @Override
    public void onTaskAdded(Task task) {
        /*
            If the state is idle/initialized currently there are no tasks to run.
            So we have to notify that tasks are available to execute
            If manager is already in executing state no need to call executing
         */
        if(!(mManagerState == STATE_TASKS_EXECUTING)){
            executeTasks();
        }
    }

    @Override
    public void onTaskRemoved(Task task) {

    }

    @Override
    public void onTaskFailed(Task task) {
        mFailedTasks.add(task);
    }

    /*
        This should actually be onTaskCompleted(K key, V value) but don't know why there is some error
        which is happening
        "Taskcompleted in taskmanager and tasklistener both methods have same erasure but neither overrides the other"
        Need to check this scenario
        So we had to typecast from object to K and V
        When the task is complete implementing subclass of taskManager to tell what should be inserted where
     */

    @Override
    public void onTaskCompleted(Object key, Object value) {
        boolean isTop;
        //This is check for initial case when both topKey and bottomKey will be null
        //if either of them is null
        if(mCache.getTopKey() == null || mCache.getBottomKey() == null){
            isTop = false;
        } else {
            isTop = insertOntop(mCache.getTopKey(), (K)key, mCache.getBottomKey());
        }
        if(isTop){
            mCache.addtoTop((K)key, (V)value);
        } else {
            mCache.addtoBottom((K)key, (V)value);
        }

        //TODO: Now the value is added to the cache and it is available for retrieval from the imageListadapter
        // We can notify to the listAdapter
    }

    /*
        we will ask the implementing subclass to decide where to insert the value
        i.e on top/bottom. We will pass the completed task key, cache top key and cache bottom key
        The implementing subclass should return true if it should be inserted on top or false if it should be insertedinBottom
     */
    public abstract boolean insertOntop(K topKey, K keytobeInserted, K bottomKey);

    public void shutDown(){
        mWorker.stop();
        mCache.clear();
        mQueue.clear();
    }

    public V getCachedResultIfavailable(K key){
        return mCache.get(key);
    }
}
