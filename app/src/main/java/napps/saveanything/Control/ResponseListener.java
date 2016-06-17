package napps.saveanything.Control;

/**
 * Created by nithesh on 6/16/2016.
 */
public interface ResponseListener<K, V> {

    /*
        Response object 'value' is passed into
     */
    void OnResponse(K key, V value);

    /*
        Error Code is passed into OnFailure method
     */
    void OnFailure(K key, int errorCode);

}
