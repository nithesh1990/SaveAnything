package napps.saveanything.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;

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

    private int destHeight;

    private int destWidth;

    private String mBitmapUri;

    private int cacheStoragePref;

    private QueueHashCache<Integer, Bitmap> cache;

    public final void setKey(int key) {
        this.key = key;
    }

    private ResponseListener<Integer, Bitmap> responseListener;

    public BitmapTask(Context mContext, int position, QueueHashCache cache, int cacheStoragePref) {
        this.mContext = mContext;
        this.cache = cache;
        this.cacheStoragePref = cacheStoragePref;
        setKey(position);
    }

    public void setImageResources(int mResizeWidth, int mResizeHeight, String mSourceUri) {
        this.destHeight = mResizeHeight;
        this.destWidth = mResizeWidth;
        this.mBitmapUri = mSourceUri;
    }


    @Override
    public Bitmap execute() {
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "CustomLib", "actual task running with position: " + getTASK_ID());

        Bitmap reSizedBitmap = null;
        try {
            //First make space for bitmaps

            //Continously loading bitmaps is creating out of memory error for many bitmaps after
            //constant capacity has been reached. The out of memory error logs is in android google drive doc
            //There was one more reason for images not loading. Even though bitmapUri was not null we were getting
            //null pointer exception from Uri.parse
            //we have to scale down images as much as possible and try another method of getting file
            //instead of uri like getting fileinoutstream or file object
            //determine sample size based on required width and height
            //InputStream sourceStream = mContext.getContentResolver().openInputStream(Uri.parse(mBitmapUri));

            int capacity = BitmapManager.BITMAP_HOLDING_CAPACITY;
            Bitmap replaceBitmap1 = cache.get((int) (getTASK_ID() + capacity));
            Bitmap replaceBitmap2 = cache.get((int) (getTASK_ID() - capacity));
            if (replaceBitmap1 != null && !replaceBitmap1.isRecycled()) {
                replaceBitmap1.recycle();
                responseListener.OnStatus((int) (getTASK_ID() + capacity), ResponseListener.CLEAR_BITMAP_SPACE);
                replaceBitmap1 = null;
            }
            //The next condition is not added in else part of above condition because at any point of time
            //only one of them should have value and other should be null which means only one of them should
            //be present in cache at any point of time. if the other one is present remove that also
            if (replaceBitmap2 != null && !replaceBitmap2.isRecycled()) {
                replaceBitmap2.recycle();
                responseListener.OnStatus((int) (getTASK_ID() - capacity), ResponseListener.CLEAR_BITMAP_SPACE);
                replaceBitmap2 = null;
            }
            //Make them garbage collected so that there is enough space left for each insertion
            InputStream sourceStream = mContext.getContentResolver().openInputStream(Uri.parse(mBitmapUri));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(sourceStream, null, options);

            int sourceHeight = options.outHeight;
            int sourceWidth = options.outWidth;
            sourceStream.close();
            int samplesize = 1;
            if (sourceHeight >= destHeight || sourceWidth >= destWidth) {
                while ((sourceHeight / samplesize) > destHeight
                        && (sourceWidth / samplesize) > destWidth) {
                    samplesize *= 2;
                }
            }

            options.inJustDecodeBounds = false;
            options.inSampleSize = samplesize;
            options.inMutable = true;
            sourceStream = mContext.getContentResolver().openInputStream(Uri.parse(mBitmapUri));

            reSizedBitmap = BitmapFactory.decodeStream(sourceStream, null, options);

            //To scale image uniformally to sit inside the dimensions we have included centerinside scaletype in imageview and see what happens

//            int width = resizedBitmap.getWidth();
//
//            int height = resizedBitmap.getHeight();
//            //we have set scaleType as centercrop in xml imageview so it will automatically centercrop
//            //here we have to specify the width and height that's it
//            //we will create square image based on width so
//            int boxSize = width;
//            int startx = 0;
//            int starty = 0;
//            if(width > boxSize){
//                startx = (width - boxSize)/2;
//            }
//
//            if(height > boxSize){
//                starty = (height - boxSize)/2;
//            }
//
//            sourceStream.close();
            //resizedBitmap =
            //Do not specify scaling in terms of width and height becaue android will stretch and
            //expand the image to scale down to that specific width and height
            //instead scale down using sample size and then do cropping to get required width and height

//            resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, boxSize, boxSize, true);
            if (reSizedBitmap != null) {
                AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "CustomLib", "cache size check before inserting: " + cache.getSize());
                responseListener.OnSuccess((int) getTASK_ID(), reSizedBitmap);
                cache.addItem((int) getTASK_ID(), reSizedBitmap, (int) (getTASK_ID() + capacity), (int) (getTASK_ID() - capacity));
            }
//            if(cacheStoragePref == cache.STORAGE_PREF_BOTTOM){
//                AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "execute()", "adding to bottom with item pos: "+getTASK_ID());
//
//                cache.addtoBottom((int)getTASK_ID(), resizedBitmap);
//            } else if(cacheStoragePref == cache.STORAGE_PREF_TOP){
//                AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "execute()", "adding to top with item pos: "+getTASK_ID());
//                cache.addtoTop((int)getTASK_ID(), resizedBitmap);
//            }
        } catch (Exception e) {
            AppLogger.addLogMessage(AppLogger.ERROR, this.getClass().getSimpleName(), "execute()", "Exception Message: " + e.getMessage());
            e.printStackTrace();
        } finally {
            return reSizedBitmap;
        }
    }

    @Override
    public void setResponseListener(ResponseListener<Integer, Bitmap> responseListener) {
        this.responseListener = responseListener;
    }

}
