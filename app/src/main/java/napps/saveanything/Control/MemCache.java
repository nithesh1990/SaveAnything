package napps.saveanything.Control;

import android.app.Instrumentation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nithesh on 6/4/2016.
 */
public class MemCache<K, V> implements Cache<K, V>{

    HashMap<Integer, Object> cache;
    ArrayList<String> al;
    private static MemCache ourInstance;

    private int mCacheSize;

    public static MemCache getInstance() {
        if(ourInstance == null){
            ourInstance = new MemCache();
        }
        return ourInstance;
    }

    private MemCache() {
    }

    @Override
    public void clear() {

    }

    @Override
    public void add(K Key, V value) {

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
        this.mCacheSize = cacheSize;
    }


}
