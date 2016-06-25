package napps.saveanything.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import napps.saveanything.Utilities.AppLogger;

/**
 * Created by nithesh on 6/18/2016.
 */
public class BitmapTask extends Task<Integer, Bitmap> {


    private int key;

    private Context mContext;

    private static final String CLASS_TAG = BitmapTask.class.getSimpleName();

    private int mDeviceHeight;

    private int mDeviceWidth;

    private String mBitmapUri;

    private int cacheStoragePref;

    private QueueHashCache<String, Bitmap> cache;

    public final int getKey() {
        return key;
    }

    public final void setKey(int key) {
        this.key = key;
    }


    public BitmapTask(Context mContext, int position, QueueHashCache cache, int cacheStoragePref) {
        this.mContext = mContext;
        this.cache = cache;
        this.cacheStoragePref = cacheStoragePref;
        setKey(position);
    }

    public void setImageResources(int mResizeWidth, int mResizeHeight, String mSourceUri) {
        this.mDeviceHeight = mResizeHeight;
        this.mDeviceWidth = mResizeWidth;
        this.mBitmapUri = mSourceUri;
    }


    @Override
    public Bitmap execute() {
        Bitmap resizedBitmap = null;
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "execute()", "actual task running with position: "+getTASK_ID());
        try {
            InputStream sourceStream = mContext.getContentResolver().openInputStream(Uri.parse(mBitmapUri));

            resizedBitmap = BitmapFactory.decodeStream(sourceStream);

            int width = resizedBitmap.getWidth();
            
            int height = resizedBitmap.getHeight();
            //we have set scaleType as centercrop in xml imageview so it will automatically centercrop
            //here we have to specify the width and height that's it
            //we will create square image based on width so
            int boxSize = mDeviceWidth;
            int startx = 0;
            int starty = 0;
            if(width > boxSize){
                startx = (width - boxSize)/2;
            }

            if(height > boxSize){
                starty = (height - boxSize)/2;
            }

            sourceStream.close();
            resizedBitmap = Bitmap.createBitmap(resizedBitmap, startx, starty, boxSize, boxSize);
            //resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, , mDeviceHeight, true);
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "execute()", "cache size check before inserting: "+cache.getSize());

            if(cacheStoragePref == cache.STORAGE_PREF_BOTTOM){
                cache.addtoBottom(mBitmapUri, resizedBitmap);
            } else if(cacheStoragePref == cache.STORAGE_PREF_TOP){
                cache.addtoTop(mBitmapUri, resizedBitmap);
            }
        } catch(FileNotFoundException e){
            AppLogger.addLogMessage(AppLogger.ERROR, this.getClass().getSimpleName(), "execute()", "File not found exception in decoding bitmap");
        } catch(IOException e){
            AppLogger.addLogMessage(AppLogger.ERROR, this.getClass().getSimpleName(), "execute()", "Exception during inputstream closing in decoding bitmap");
        }finally {
            return resizedBitmap;
        }
    }
}
