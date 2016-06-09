package napps.saveanything.Control;

import android.support.v4.util.LruCache;

/**
 * Created by nithesh on 6/4/2016.
 */
/*  A Queue hash which contains constant set of items at any point of time and items are added to end and removed from beginning

   At any point of time CONSTANT_CAPACITY of items STAY in the cache

   Items can be added to cache or removed from the cache.
   There will be a double ended queue which will actually hold objects.
   This is fine for adding and removing as both are 0(1) operations. This is like a growing-shrinking chain.
   But we need get operation to be 0(1) which is the most important operation for cache.
   In Dequeue it is not possible to provide O(1) operation for get.
   So we use a hashmap to store the reference of objects present in dequeue as and when the object is added/removed
   HashMap is updated accordingly.
   Node {
        Node Previous;
        Bitmap image;
        Node next;
   }
   HashMap<Integer, Node>, get -> hashmap.get(i).image will return the bitmap.
   
 */
public class QueueHashCache<K, V> implements Cache<K, V> {

    //This is the constant set/ maximum set of elements that cache holds. An object can be stored here.
    //Objects are added in circular queue fashion
    //Ex: If constant capacity is 10 then objects are inserted in 0,1,2,....10 and once it's full it starts from 0, 1, 2, 3...10
    //All the object references are strong references
    //Since this cache is element based cache and not memory based cache.
    // Users should be careful in allocating objects
    private int CONSTANT_CAPACITY;

    private int lastInsertIndex;
     LruCache<String, String> cac;

    @Override
    public void clear() {

    }

    @Override
    public void add(V value) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void setMemoryLimit(long bytes) {

    }

    @Override
    public void OnExceedLimit() {

    }


    @Override
    public void setCacheSize(int cacheSize) {
        CONSTANT_CAPACITY = cacheSize;
    }



  }
