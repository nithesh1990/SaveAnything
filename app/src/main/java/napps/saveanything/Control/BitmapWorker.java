package napps.saveanything.Control;

import android.net.Uri;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by nithesh on 6/6/2016.
 */
/*
    Background Bitmap Logic
    Background processing of images is done based on item position of getview() as this is reliable
    consistent incrementing/decrementing position.
    We will be passing 5 references - cache, position, imageUri, Handler, ImageView to background thread.
    Step 1: Image is extracted using imageUri and put into cache using position(synchronized operation)
    Step 2: Once Image is successfully processed into cache a status signal is sent 'IMAGE_READY'/'FILE_NOT_FOUND'/'PROCESSING_ERROR etc
            is sent to calling thread using Handler and corresponding imageView reference is passed
    Step 3: In handleMessage() the ImageView is Checked and if it is currently visible the corresponding bitmap is set to the imageview or else bitmap stays in the cache
            Here if some status come as FILE_NOT_FOUND, then drawable resource is set as bitmap and that uri is added notfoundlist.
            Next time if the same uri is requested first filenotfoundlist is checked to see if the uri is there. If its there then immediately that drawable resource is set
            saving background processing. FileNotfoundlist should be HashSet in order to maintain get() complexity to O(1)

    The above steps form a single image processing task.
    Two ways of processing can be done
    1. Lazy Processing : Process only when it is requested
        a. When requested for image, check if it's in cache. return if it's there or else start the process of getting it
    2. Eager Processing : Process a batch of images before it is requested. Requested batch is divided into

        TOP BUFFER
        ACTUAL
        BOTTOM BUFFER

       Keep checking the requested position. If in case the requested position falls into BOTTOM BUFFER get
       new set and make that BOTTOM BUFFER, Remove TOP BUFFER Noew actual, bottom buffer and new buffer become top buffer, actual and bottom buffer.
       These are just indexes which keep updating. New buffer is added to top buffer if the request falls in TOP BUFFER
 */
public class BitmapWorker implements Runnable{

    //HashMap is selected because of the followinf reasons
    //1. We wanted a map which stores keys and values
    //2. We could have used Arraylist as it is dynamically resizing having O(1) complexity for get and put and we could have used indexes as keys
    //      as in our case keys are unique from 0 to n. The reason for not using is
    //      a. Sometimes we want to grow arraysize up and not only below when new items are added to the top. So array list index can't go beyond 0 i.e -1, 0
    HashMap<Integer, Uri> sourceUris;
     public BitmapWorker() {
    }

    @Override
    public void run() {

    }
}
