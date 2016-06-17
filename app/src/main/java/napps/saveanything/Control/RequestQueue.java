package napps.saveanything.Control;

import java.util.concurrent.ConcurrentLinkedDeque;
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
public class RequestQueue<V> {

    /*
        This is made volatile for the following reasons
        1. We want queue to be synchronized but not strictly. If we make it synchronized then it degrades performane
            because everytime lock is acquired by thread operation is performed and then released. Since the request queue is
             constantly added and updated making synchronized would drastically degrade the performance

        Using AtomicReference which internally applies volatile keyword properties
     */

    private volatile AtomicReference<LinkedBlockingDeque<Request>> mQueue;

    private static RequestQueue sInstance;

    private int mQueueSize;

    public static RequestQueue getInstance(){
        if(sInstance == null){
            synchronized (RequestQueue.class){
                if(sInstance == null){
                    sInstance  = new RequestQueue();
                }
            }
        }
        return sInstance;
    }

    private RequestQueue(){
        /*
            Creating new atomicReference for queue and passing the value to be set i.e initializing it.
            Later we can access and change its contents
         */
        mQueue = new AtomicReference<LinkedBlockingDeque<Request>>(new LinkedBlockingDeque<Request>());
    }

    public void initializeQueue(QueueHashCache cache){
        mQueueSize = cache.getCONSTANT_CAPACITY();
    }

    public void add(Request request){
        if(mQueue.get().size() >= mQueueSize){
            mQueue.get().removeFirst();
        }
        mQueue.get().add(request);
    }

    public Request getNext(){
        if(mQueue.get().size() <= 0){
            // throw new queueEmpty exception
        }
        return mQueue.get().getFirst();
    }
}
