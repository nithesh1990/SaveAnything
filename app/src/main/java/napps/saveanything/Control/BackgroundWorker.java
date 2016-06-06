package napps.saveanything.Control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by nithesh on 5/28/2016.
 */
public class BackgroundWorker implements RejectedExecutionHandler{



    private static int REJECTED_TASK_WAIT_TIME = 1000;
    //pool size can be bounded or unbounded.
    //A bounded queue (for example, an ArrayBlockingQueue) helps prevent resource exhaustion when used with finite maximumPoolSizes, but can be more difficult to tune and control.
    //Using large queues and small pools minimizes CPU usage, OS resources, and context-switching overhead, but can lead to artificially low throughput.
    //Using an unbounded queue (for example a LinkedBlockingQueue without a predefined capacity) will cause new tasks to wait in the queue when all corePoolSize threads are busy.
    private static int NUMBER_OF_CORES;

    private static int INITIAL_THREAD_POOL_SIZE;

    //we need a queue of threads(i.e runnables) a queue is implemented using arrays linked lists but our need is this
    //1. We can't directly use a queue because if a big image is in processing and smaller images which can be
    //   finished faster need to wait for the tasks to get finished
    //2. So we need a better functionality to receive jobs and finish them using intelligent techniques
    //There is already a job scheduler api in android but that is used for scheduling tasks at right times
    //We need to specify the constraints in which the job should execute. Example of constraints are:
    //Run this when the phone is plugged in
    //Run this when on an unmetered connection, but only run for one hour
    //Wait at least 10 minutes to start this job initially, and then whenever the device is idle
    //Every 'x' minutes, whenever network is available let me (app) know so that I (app) can do a small health check.
    // A queue is called blocking queue : Attempts to put an element into a full queue will result in the operation blocking; attempts to take an element from an empty queue will similarly block.

    //for now let's consider only blocking queue and there are 3 types of queues - direct handoff(no waiting policy), unbounded queue(any number of jobs can wait), bounded queue(fixed number of jobs can wait after that jobs get rejected)
    // Once core thread pool size have been reached we need queue for the tasks to wait
    private final BlockingQueue<Runnable> jobQueue;


    //We have a queue and we keep adding jobs to it but we need some executor/executor service for that
    //which takes the task from the queue and executes it(i.e calls run method) keeps the thread idle for some time
    //and shuts down the thread after idle time and removes from the threadpool

    private final ThreadPoolExecutor jobExecutor;

    //This is keep alive time the thread is kept alive before shutdown. After work is finished in each thread it is kept for keep_alive_time in order to avoid killing of threads
    //Essentially this just allows you to control the number of threads left in the idle pool. If you make this too small (for what you are doing), you will be creating too many threads. If you make it too large, you will be consuming memory/threads you don't need to.
    // if you keep this small then threads will be kept removed as soon as they reach alive time and a job occurs just after the alive time a new thread needs to be created
    // If we make it too large threads are kept simply idle consuming resources, memory and battery
    // we need to have right balance between
    // in our case we give jobs at once and not at time to time. If we are reusing the threads we should not kill them
    //Need to think about it a bit
    private final long KEEP_ALIVE_TIME = 500;

    //keep alive time unit
    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    // SingleTon pattern is very much essential because we have to make sure only one thread pool is running
    //If we keep on creating objects which in turn creates multiple threadpools which is a bad implementation
    private static BackgroundWorker ourInstance;

    public static BackgroundWorker getInstance() {
        if(ourInstance == null){
            ourInstance = new BackgroundWorker();
        }
        return ourInstance;
    }


    //There is one more way of creating singleton instance
    //Initialize the object in static block
    //static {
    //    ourInstance = new BackgroundWorker();
    //}
    //This will create the instance as soon as the class is loaded
    //Return this instance on getInstance()
    //But this is disadvantage as the instance stays in heap unnecessiraly even though if it's not used
    private BackgroundWorker() {
        // We need to get the no. of cores because thread pool size
        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        //We make this default to 1 in dual core/quad core systems which pre creates that thread
        //avoiding overhead of creating a thread. But for single core processors let's keep it to 0
        //This is also called core pool size
        if(NUMBER_OF_CORES > 1){
            INITIAL_THREAD_POOL_SIZE = 1;
        } else {
            INITIAL_THREAD_POOL_SIZE = 0;
        }


        //this is a queue of runnables
        jobQueue = new LinkedBlockingQueue<Runnable>();

        //while initializing thread pool executor make sure you have specified number of threads initial
        // and no. of threads it can use to run at maximum , keep alive time(i.e time to keep threads alive after the job is done)
        // and the thread queue bcoz this takes only classes that extend blocking queue
        //By setting corePoolSize and maximumPoolSize the same, you create a fixed-size thread pool. By setting maximumPoolSize to an essentially unbounded value such as Integer.MAX_VALUE, you allow the pool to accommodate an arbitrary number of concurrent tasks. Most typically, core and maximum pool sizes are set only upon construction, but they may also be changed dynamically using setCorePoolSize(int) and setMaximumPoolSize(int).
        //This is actually bad due to the resources limited on mobile device
        // we cannot use newCachedThreadpool() or newFixedThreadPool() because in those maximum cores is decided internally which is Integer.MAX_VALUE
        // and if we submit 10 save image tasks simultaneously it might span to 10 threads which is dangerous considering limited resources in mobile
        // Although they are efficient we can't use them directly so we are using android provided ThreadPoolExecutor
        // here the initial thread pool size should be 0 and it will spawn till max threads and keep alive time as litel as possible
        jobExecutor = new ThreadPoolExecutor(INITIAL_THREAD_POOL_SIZE, NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, jobQueue, this);


    }

    //New tasks submitted in method execute(Runnable) will be rejected when the Executor has been shut down
    //and also when the Executor uses finite bounds for both maximum threads and work queue capacity, and is saturated.
    //In either case, the execute method invokes the rejectedExecution(Runnable, ThreadPoolExecutor) method of its RejectedExecutionHandler.
    //This is important for us we are going to decide pool size based on device resources(ram, cpu cores)
    //If we have set small pool size for low end device and user gives large tasks at a time then
    //tasks might get rejected so we have to handle those tasks
    //There are specific handler policies for this. So read before implementing
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try{
            r.wait(REJECTED_TASK_WAIT_TIME);
            BackgroundWorker.getInstance().jobExecutor.execute(r);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void addTasktoExecute(Runnable task){
        jobExecutor.execute(task);
    }
}
