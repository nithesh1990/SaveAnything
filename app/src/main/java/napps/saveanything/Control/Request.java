package napps.saveanything.Control;

/**
 * Created by nithesh on 6/16/2016.
 */
public class Request<K, V> {

    public static final int PRIORITY_HIGH = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 2;

    private int mPriority;

    private K key;

    private ResponseListener<K, V> mResponseListener;


    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public int getmPriority() {
        return mPriority;
    }

    public void setmPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public ResponseListener<K, V> getmResponseListener() {
        return mResponseListener;
    }

    public void setmResponseListener(ResponseListener<K, V> mResponseListener) {
        this.mResponseListener = mResponseListener;
    }

}
