package napps.saveanything.Control;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by nithesh on 6/16/2016.
 */
/*
    Linked Blocking Queue is used for the following reasons
    1. This is a bounded queue which is what we require
    2.  Nodes are created dynamically which saves memory
    3. It's a dequeue supporting operations at both ends
 */
public class FutureTaskQueue<V> {

    /*
        This is made volatile for the following reasons
        1. We want queue to be synchronized but not strictly. If we make it synchronized then it degrades performane
            because everytime lock is acquired by thread operation is performed and then released. Since the request queue is
             constantly added and updated making synchronized would drastically degrade the performance

        Using AtomicReference which internally applies volatile keyword properties
     */

    private volatile AtomicReference<LinkedBlockingDeque<Future<V>>> mQueue;

    private static FutureTaskQueue sInstance;

    private int mQueueSize;

    public static FutureTaskQueue getInstance(){
        if(sInstance == null){
            synchronized (FutureTaskQueue.class){
                if(sInstance == null){
                    sInstance  = new FutureTaskQueue();
                }
            }
        }
        return sInstance;
    }

    private FutureTaskQueue(){
        /*
            Creating new atomicReference for queue and passing the value to be set i.e initializing it.
            Later we can access and change its contents
         */
        mQueue = new AtomicReference<LinkedBlockingDeque<Future<V>>>(new LinkedBlockingDeque<Future<V>>());
    }

    public void initializeQueue(int capacity){
        mQueueSize = capacity;
    }

    public void add(Future<V> task){
        if(mQueue.get().size() >= mQueueSize){
            Future<V> firstTask = mQueue.get().getFirst();
            if(!firstTask.isDone()) {
                //wee pass true to interrupt
                firstTask.cancel(true);
            }
            mQueue.get().remove(firstTask);

        }
        mQueue.get().add(task);
    }

    public Future<V> getNext(){
        if(mQueue.get().size() <= 0){
            // throw new queueEmpty exception
        }
        return mQueue.get().getFirst();
    }

    public boolean isEmpty(){
        return mQueue.get().isEmpty();
    }

    public void clear(){
        mQueue.get().clear();
    }

    public void removeTask(Task task){
        mQueue.get().remove(task);
    }

}
