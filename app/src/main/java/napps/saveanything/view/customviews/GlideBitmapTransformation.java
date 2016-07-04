package napps.saveanything.view.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import napps.saveanything.view.Fragments.ImageFragment;

/**
 * Created by nithesh on 6/26/2016.
 */
public class GlideBitmapTransformation extends BitmapTransformation {

    private int imageBoxsize;

    private int imagePosition;

    private int viewMode;

    public GlideBitmapTransformation(Context context, int imageBoxsize, int imagePosition, int viewMode) {
        super(context);
        this.imageBoxsize = imageBoxsize;
        this.imagePosition = imagePosition;
        this.viewMode = viewMode;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();

        int height = toTransform.getHeight();
        //we have set scaleType as centercrop in xml imageview so it will automatically centercrop
        //here we have to specify the width and height that's it
        //we will create square image based on width so
//        if(width <= imageBoxsize && height < imageBoxsize){
        if(viewMode == ImageFragment.GRID_MODE){
            return Bitmap.createScaledBitmap(toTransform, imageBoxsize, imageBoxsize, true);
        } else {
            return toTransform;
        }//        }
//        int startx = 0;
//        int starty = 0;
//        if(width > imageBoxsize){
//            startx = (width - imageBoxsize)/2;
//        }
//
//        if(height > imageBoxsize){
//            starty = (height - imageBoxsize)/2;
//        }
//
//        return Bitmap.createBitmap(toTransform, startx, starty, imageBoxsize, imageBoxsize);


    }

    @Override
    public String getId() {
        return String.valueOf(imagePosition);
    }
}
