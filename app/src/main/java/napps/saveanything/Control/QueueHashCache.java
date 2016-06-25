package napps.saveanything.Control;

import android.support.v4.util.LruCache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

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

    Initially we used
 */
public class QueueHashCache<K, V> implements Cache<K, V> {

    //This is the constant set/ maximum set of elements that cache holds. An object can be stored here.
    //Objects are added in circular queue fashion
    //Ex: If constant capacity is 10 then objects are inserted in 0,1,2,....10 and once it's full it starts from 0, 1, 2, 3...10
    //All the object references are strong references
    //Since this cache is element based cache and not memory based cache.
    // Users should be careful in allocating objects
    private int CONSTANT_CAPACITY;

    private static QueueHashCache sInstance;

    public static final int STORAGE_PREF_TOP = 0;

    public static final int STORAGE_PREF_BOTTOM = 1;
    /*
        These top and bottom key references are required so that the values are checked and
        value V is put on top or bottom respectively.
        if the incoming value is less than topKey it is inserted at top and
        if it is greater than bottom key it is inserted at bottom
     */
    private K topKey;

    private K bottomKey;

    public static QueueHashCache getsInstance(){
        if(sInstance == null){
            synchronized (QueueHashCache.class){
                if(sInstance == null){
                    sInstance = new QueueHashCache();
                }
            }
        }

        return sInstance;
    }
    private int lastInsertIndex;

    private AtomicReference<LinkedHashMap<K, V>> objReference;

    private LinkedList<Node> objCache;

    private QueueHashCache(){

    }

    public void initialize(int CONSTANT_CAPACITY) {
        setCacheSize(CONSTANT_CAPACITY);
        objCache = new LinkedList<Node>();
        objReference = new AtomicReference<LinkedHashMap<K, V>>(new LinkedHashMap<K, V>());

    }

    @Override
    public void clear() {
        //This is a clever way of
        objCache.clear();
        objReference.get().clear();
    }

    @Override
    public void save(K key, V value) {
        /*
            Let's leave this unimplemented. This method is to maintain cache interface method standards
            Because cache just says to save and shouldn't specify the position because it is violation for
            generic cache implementation
            Although there is no use of this method it is just for
         */
    }

    public void addtoBottom(K key, V value) {
        int size = objCache.size();
        //if size has reached constant capacity then
        if(size >= CONSTANT_CAPACITY){
            Node removalNode = objCache.getFirst();
            objReference.get().remove(removalNode.getKey());
            objCache.removeFirst();
            //After removing the top item, top key should be updated appropriately
            topKey = (K)objCache.getFirst().getKey();
        }
        Node addNode = new Node(key, value);
        objCache.addLast(addNode);
        objReference.get().put((K)addNode.getKey(), (V)addNode.getValue());
        bottomKey = key;
        if(topKey == null){
            topKey = bottomKey;
        }
    }

    public void addtoTop(K key, V value) {
        int size = objCache.size();
        //if size has reached constant capacity then
        if(size >= CONSTANT_CAPACITY){
            Node removalNode = objCache.getLast();
            objReference.get().remove(removalNode.getKey());
            objCache.removeLast();
            bottomKey = (K)objCache.getLast().getKey();
        }
        Node addNode = new Node(key, value);
        objCache.addFirst(addNode);
        objReference.get().put((K)addNode.getKey(), (V)addNode.getValue());
        topKey = key;
        if(bottomKey == null){
            bottomKey = topKey;
        }
    }

    @Override
    public V get(K key) {

        if(objReference.get().containsKey(key)){
            return objReference.get().get(key);
        } else {
            return null;
        }
    }

    @Override
    public void setMemoryLimit(long bytes) {
        //
    }

    @Override
    public void OnExceedLimit() {

    }


    @Override
    public void setCacheSize(int cacheSize) {
        CONSTANT_CAPACITY = cacheSize;
    }


    public int getCONSTANT_CAPACITY() {
        return CONSTANT_CAPACITY;
    }

    public void setCONSTANT_CAPACITY(int CONSTANT_CAPACITY) {
        this.CONSTANT_CAPACITY = CONSTANT_CAPACITY;
    }

    public int getSize(){
        return objCache.size();
    }

    public K getBottomKey() {
        return bottomKey;
    }

    public K getTopKey() {
        return topKey;
    }


    private class Node<K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
