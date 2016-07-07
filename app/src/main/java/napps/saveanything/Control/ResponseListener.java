package napps.saveanything.Control;

/**
 * Created by nithesh on 6/16/2016.
 */
public interface ResponseListener<K, V> {

    //variables of interfaces are static and final by default
    int CLEAR_BITMAP_SPACE = 3;
    int RESULT_SUCCESS = 1;
    /*
        Response object 'value' is passed into
     */
    void OnSuccess(K key, V value);

    void OnStatus(K key, int statusCode);
    /*
        Error Code is passed into OnFailure method
     */
    void OnFailure(K key, int errorCode);

}
