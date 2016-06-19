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

    private Context mContext;

    private int mResizeHeight;

    private int mResizeWidth;

    private Uri mBitmapUri;

    public BitmapTask(Context mContext, int position) {
        this.mContext = mContext;
        setKey(position);
    }

    public void setImageResources(int mResizeWidth, int mResizeHeight, Uri mSourceUri) {
        this.mResizeHeight = mResizeHeight;
        this.mResizeWidth = mResizeWidth;
        this.mBitmapUri = mSourceUri;
    }


    @Override
    public Bitmap execute() {
        Bitmap resizedBitmap = null;
        try {
            InputStream sourceStream = mContext.getContentResolver().openInputStream(mBitmapUri);

            resizedBitmap = BitmapFactory.decodeStream(sourceStream);

            sourceStream.close();

            resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, mResizeWidth, mResizeHeight, true);

        } catch(FileNotFoundException e){
            AppLogger.addLogMessage(AppLogger.ERROR, this.getClass().getSimpleName(), "execute()", "File not found exception in decoding bitmap");
        } catch(IOException e){
            AppLogger.addLogMessage(AppLogger.ERROR, this.getClass().getSimpleName(), "execute()", "Exception during inputstream closing in decoding bitmap");
        }finally {
            return resizedBitmap;
        }
    }
}
