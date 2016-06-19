package napps.saveanything.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by nithesh on 6/18/2016.
 */
public class BitmapManager extends TaskManager<Integer, Bitmap> {

    private static final int BITMAP_HOLDING_CAPACITY = 10;

    private Context mContext;

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

    public BitmapManager(Context context) {
        this.mContext = context;
        initialize(mContext, BITMAP_HOLDING_CAPACITY);
    }

    public void setBitmap(Uri uri, int position, ImageView imageView, int requiredWidth, int requiredHeight){

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

    @Override
    public boolean insertOntop(Integer topKey, Integer keytobeInserted, Integer bottomKey) {
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
}
