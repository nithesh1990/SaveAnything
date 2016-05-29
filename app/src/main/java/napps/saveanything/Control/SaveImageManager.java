package napps.saveanything.Control;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by nithesh on 5/28/2016.
 */
public class SaveImageManager {
    private static SaveImageManager ourInstance;
    private static int NUMBER_OF_CORES;

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

    //for now let's consider only blocking queue
    private final BlockingQueue<Runnable> jobQueue;


    //We have a queue and we keep adding jobs to it but we need some executor/executor service for that
    //which takes the task from the queue and executes it(i.e calls run method) keeps the thread idle for some time
    //and shuts down the thread after idle time and removes from the threadpool

    private final ThreadPoolExecutor jobExecutor;

    //This is keep alive time the thread is kept alive before shutdown
    private final long KEEP_ALIVE_TIME = 500;

    //keep alive time unit
    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;
    public static SaveImageManager getInstance() {
        if(ourInstance == null){
            ourInstance = new SaveImageManager();
        }
        return ourInstance;
    }

    private SaveImageManager() {
        // We need to get the no. of cores because thread pool size
        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        //this is a queue of runnables
        jobQueue = new LinkedBlockingQueue<Runnable>();

        //while initializing thread pool executor make sure you have specified number of threads initial
        // and no. of threads it can use to run at maximum , keep alive time(i.e time to keep threads alive after the job is done)
        // and the thread queue bcoz this takes only classes that extend blocking queue
        jobExecutor = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, jobQueue);


    }
}
