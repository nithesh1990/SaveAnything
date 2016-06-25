package napps.saveanything.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import napps.saveanything.Utilities.Constants;
import napps.saveanything.Utilities.Utils;
import napps.saveanything.Database.DBHelper;
import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.Model.ImageInfo;

/**
 * Created by nithesh on 5/26/2016.
 */
public class SaveImageTask extends Task<Integer, Bitmap> {

    private ImageInfo mImageInfo;
    private Context mContext;

    public SaveImageTask(ImageInfo mImageInfo, Context mContext) {
        this.mImageInfo = mImageInfo;
        this.mContext = mContext;
    }

    @Override
    public Bitmap execute() {
        ContentValues contentValues = new ContentValues();

        try{
            Uri uri = Uri.parse(mImageInfo.getOriginalPath());
            String saveFileName = Constants.PREFIX_IMAGE + String.valueOf(mImageInfo.getTimestamp())+Constants.IMAGE_FORMAT_PNG;
            File saveFile = new File(Utils.getImageStoragePath(), saveFileName);
            //openInputStream is used because of the following reasons

            //Accepts the following URI schemes:
            //content (SCHEME_CONTENT)
            //android.resource (SCHEME_ANDROID_RESOURCE)
            //file (SCHEME_FILE)
            InputStream sourceStream = mContext.getContentResolver().openInputStream(uri);

            if(mImageInfo.getScaleStatus() == Constants.SCALE_DOWN){

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = mImageInfo.getScaleFactor();
                Bitmap resizedBitmap = BitmapFactory.decodeStream(sourceStream, null, options);
                FileOutputStream fileOut = new FileOutputStream(saveFile);
                //quality	int: Hint to the compressor, 0-100. 0 meaning compress for small size, 100 meaning compress for max quality. Some formats, like PNG which is lossless, will ignore the quality setting
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
                fileOut.close();
                resizedBitmap.recycle();
                contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS, Constants.SCALE_DOWN);

            } else if(mImageInfo.getScaleStatus() == Constants.SCALE_UP){
                int destWidth = mImageInfo.getSourceWidth() * mImageInfo.getScaleFactor();
                int destHeight = mImageInfo.getSourceHeight() * mImageInfo.getScaleFactor();
                Bitmap resizedBitmap = BitmapFactory.decodeStream(sourceStream, null, null);
                resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, destWidth, destHeight, true);
                FileOutputStream fileOut = new FileOutputStream(saveFile);
                //quality	int: Hint to the compressor, 0-100. 0 meaning compress for small size, 100 meaning compress for max quality. Some formats, like PNG which is lossless, will ignore the quality setting
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
                fileOut.close();
                resizedBitmap.recycle();
                contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS, Constants.SCALE_UP);
            } else {

                FileOutputStream fileout = new FileOutputStream(saveFile);
                byte[] buffer = new byte[(int)Utils.getBufferMemorySize(mContext)];
                int bufferlen;

                while((bufferlen = sourceStream.read(buffer)) > 0 ){
                    fileout.write(buffer, 0, bufferlen);
                }
                fileout.flush();
                fileout.close();
                sourceStream.close();
                contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS, Constants.SCALE_NEUTRAL);

            }

            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_FACTOR, mImageInfo.getScaleFactor());
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH, Uri.fromFile(saveFile).toString());
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_SAVED);
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_DESC, mImageInfo.getDesc());

        } catch(NullPointerException e){
            //uri path is null which shouldn't be possible
            //delete the contents from db
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch(FileNotFoundException e){
            // file removed from source
            //delete the contents from db
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch (IOException e){
            //some error happened while copying file
            //update db with error
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch (Utils.OutofStorageError e){
            //disk full
            //update proper error
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch (Exception e){
            //Any other exception
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        }
        finally {

            SQLiteDatabase db = DBHelper.getInstance(mContext).getWritableDatabase();
            //imageId = mImageInfo.getImageId() cannot be used because SQLite thinks that 'I12787284y80' is actually a column name, rather than a string/text literal.
            //So we have to wrap it to look like 'I12787284y80'
            db.update(DatabaseContract.ImageBoard.TABLE_NAME, contentValues, DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID+" = "+"'"+mImageInfo.getImageId()+"'", null);

        }
    return null;
    }
}
