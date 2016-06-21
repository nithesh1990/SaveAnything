package napps.saveanything.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.Utilities.AppLogger;

/**
 * Created by nithesh on 6/18/2016.
 */
public class BitmapManager extends TaskManager<Integer, Bitmap> {

    private static final String CLASS_TAG = BitmapManager.class.getSimpleName();
    private static final int BITMAP_HOLDING_CAPACITY = 10;

    private Context mContext;


    private QueueHashCache<Integer, Bitmap> mCache;

    private static BitmapManager sInstance;

    public static BitmapManager getInstance(){
        if(sInstance == null){
            synchronized (BitmapManager.class){
                if(sInstance == null){
                    sInstance = new BitmapManager();
                }
            }
        }
        return sInstance;
    }

    private BitmapManager(){

    }

    public void initializeBM(Context context) {
        this.mContext = context;
        initialize(mContext, BITMAP_HOLDING_CAPACITY);
        mCache = QueueHashCache.getsInstance();
        mCache.initialize(BITMAP_HOLDING_CAPACITY);

    }

    public void setBitmap(Uri uri, int position, ImageView imageView, int requiredWidth, int requiredHeight){
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "setBitmap()", "");
        Bitmap cachedBitMap = (Bitmap) getCachedResultIfavailable(position);
        if(cachedBitMap != null){
            if(imageView != null){
                imageView.setImageBitmap(cachedBitMap);
            }
        } else {
            BitmapTask bitmapTask = new BitmapTask(mContext, position);
            bitmapTask.setImageResources(requiredWidth, requiredHeight, uri);
            addTask(bitmapTask);
        }
    }
    //TODO:
    //This is a costly operation which we are doing in UI thread and hence there was a hit in the performance
    //and since different tasks complete at different times we can't say that tasks get finished serially and this
    //was causing issues because we were expecting it serially.
    //We don't need a separate Node reference. We can just keep Integer references to simulate queue functions i.e top and bottom add/remove
    //and we can put/retreive those corresponding values from the linkedHashmap which will save lot of memory and space.
    //Simulating queue functionality due to items arriving non serially after results. There can be 2 possible solutions for this
    //1. Implement an array functionality and simulate those queue operations
    //2. Perform these add/remove top/bottom during the add of tasks itself
    @Override
    public void postResult(long taskId, Bitmap value) {

        boolean isTop;
        //This is check for initial case when both topKey and bottomKey will be null
        //if either of them is null
        if(mCache.getTopKey() == null || mCache.getBottomKey() == null){
            isTop = false;
        } else {
            isTop = insertOntop(mCache.getTopKey(), (K)key, mCache.getBottomKey());
        }
        if(isTop){
            mCache.addtoTop((K)key, (V)value);
        } else {
            mCache.addtoBottom((K)key, (V)value);
        }

        //TODO: Now the value is added to the cache and it is available for retrieval from the imageListadapter
        // We can notify to the listAdapter


    }

    //This will return true if it should be inserted on top or false if it should be insertedinBottom


    public boolean insertOntop(Integer topKey, Integer keytobeInserted, Integer bottomKey) {
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "insertOnTop() ", "actual key value: "+keytobeInserted.intValue());
        int top = topKey.intValue();
        int actual = keytobeInserted.intValue();
        int bottom = bottomKey.intValue();
        if(actual > bottom){
            return false;
        } else if(actual < top){
            return true;
        }
        //TODO:
        //Here we have to handle else case where actual value is between top and bottom which should not happen

        return false;
    }

    public V getCachedResultIfavailable(K key){
        return mCache.get(key);
    }

}
