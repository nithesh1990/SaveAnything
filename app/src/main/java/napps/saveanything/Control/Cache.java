package napps.saveanything.Control;

/**
 * Created by nithesh on 6/4/2016.
 */
/*
    A Cache interface that specifies the basic function a cache should perform.
    This implementation can be LRU/FIFO/LIFO which can be implemented by classes
 */
public interface Cache<K, V> {

    /*
        Clear the cache
     */
    public void clear();

    /*
        Add items to cache
     */
    public void add(V value);

    /*
        Get items from cache
        Do not specify the paramter key to get values
     */
    public V get(K Key);

    /*
        Set Memory limit in bytes
     */
    public void setMemoryLimit(long bytes);

    /*
        Handle the situation of memory limit exceeded
     */
    public void OnExceedLimit();

    /*
        Get the number of elements in Cache
     */
    public void setCacheSize(int cacheSize);

}
