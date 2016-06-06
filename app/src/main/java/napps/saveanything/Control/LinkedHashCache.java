package napps.saveanything.Control;

import android.support.v4.util.LruCache;

/**
 * Created by nithesh on 6/4/2016.
 */
/*  A Queue hash which contains constant set of items at any point of time and items are added to end and removed from beginning
    This cache is list of items divided into 3 sections

        DETACH
        STAY
        ADD

   At any point of time CONSTANT_CAPACITY of items STAY in the cache

   Items can be added to cache or removed from the cache. Items can be added
 */
public class LinkedHashCache<K, V> implements Cache<K, V> {

    private static int CONSTANT_CAPACITY;
    LruCache<String, String> cac;

    @Override
    public void clear() {

    }

    @Override
    public void add(V value) {

    }

    @Override
    public V get() {
        return null;
    }

    @Override
    public void setMemoryLimit(long bytes) {

    }

    @Override
    public void OnExceedLimit() {

    }

    @Override
    public int size() {
        return 0;
    }
}
