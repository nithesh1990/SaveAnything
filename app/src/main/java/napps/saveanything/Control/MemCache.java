package napps.saveanything.Control;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nithesh on 6/4/2016.
 */
public class MemCache<K, V>  {

    HashMap<Integer, String> hm;
    ArrayList<String> al;
    private static MemCache ourInstance = new MemCache();

    public static MemCache getInstance() {
        return ourInstance;
    }

    private MemCache() {
    }
}
