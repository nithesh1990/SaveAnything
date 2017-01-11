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

import napps.saveanything.Database.RealmContentProvider;
import napps.saveanything.Model.Builder;
import napps.saveanything.Model.Image;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.Utilities.Utils;
import napps.saveanything.Database.DBHelper;
import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.view.Activities.SaveImageActivity;

/**
 * Created by nithesh on 5/26/2016.
 */
public class SaveImageTask extends Task<Integer, Bitmap> {

    private Builder.ImageBuilder imageBuilder;
    private Context mContext;


    public SaveImageTask(Builder.ImageBuilder imageBuilder, Context mContext) {
        this.imageBuilder = imageBuilder;
        this.mContext = mContext;
    }

    @Override
    public Bitmap execute() {
        //TODO check if there is any exception in creating the file
        //sometimes it happens that storage is not mounted and there is an exception
        try{
            Uri uri = Uri.parse(imageBuilder.originalPath);
            String saveFileName = Constants.PREFIX_IMAGE + String.valueOf(imageBuilder.timestamp)+Constants.IMAGE_FORMAT_PNG;
            File saveFile = new File(Utils.getImageStoragePath(), saveFileName);
            //openInputStream is used because of the following reasons

            //Accepts the following URI schemes:
            //content (SCHEME_CONTENT)
            //android.resource (SCHEME_ANDROID_RESOURCE)
            //file (SCHEME_FILE)
            InputStream sourceStream = mContext.getContentResolver().openInputStream(uri);

            if(imageBuilder.scaleStatus == Constants.SCALE_DOWN){

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = imageBuilder.scaleFactor;
                Bitmap resizedBitmap = BitmapFactory.decodeStream(sourceStream, null, options);
                FileOutputStream fileOut = new FileOutputStream(saveFile);
                //quality	int: Hint to the compressor, 0-100. 0 meaning compress for small size, 100 meaning compress for max quality. Some formats, like PNG which is lossless, will ignore the quality setting
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
                fileOut.close();
                resizedBitmap.recycle();
                imageBuilder.scaleStatus(Constants.SCALE_DOWN);
                //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS, Constants.SCALE_DOWN);

            } else if(imageBuilder.scaleStatus == Constants.SCALE_UP){
                int destWidth = imageBuilder.sourceWidth * imageBuilder.scaleFactor;
                int destHeight = imageBuilder.sourceHeight * imageBuilder.scaleFactor;
                Bitmap resizedBitmap = BitmapFactory.decodeStream(sourceStream, null, null);
                resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, destWidth, destHeight, true);
                FileOutputStream fileOut = new FileOutputStream(saveFile);
                //quality	int: Hint to the compressor, 0-100. 0 meaning compress for small size, 100 meaning compress for max quality. Some formats, like PNG which is lossless, will ignore the quality setting
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
                fileOut.close();
                resizedBitmap.recycle();
                imageBuilder.scaleStatus(Constants.SCALE_UP);
                //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS, Constants.SCALE_UP);
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
                imageBuilder.scaleStatus(Constants.SCALE_NEUTRAL);
                //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS, Constants.SCALE_NEUTRAL);

            }

            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_FACTOR, mImage.getScaleFactor());
            imageBuilder.savedPath(Uri.fromFile(saveFile).toString());
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH, Uri.fromFile(saveFile).toString());
            imageBuilder.status(Constants.STATUS_IMAGE_SAVED);
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_SAVED);
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_DESC, mImage.getDesc());

            //TODO: These different catch statements are only for debug purpose. In future might include trigger different notifications
            // based on type of error. If not remove all these and put a single catch
        } catch(NullPointerException e){
            //uri path is null which shouldn't be possible
            //delete the contents from db
            imageBuilder.status(Constants.STATUS_IMAGE_PROCESS_ERROR);
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch(FileNotFoundException e){
            // file removed from source
            //delete the contents from db
            imageBuilder.status(Constants.STATUS_IMAGE_PROCESS_ERROR);
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch (IOException e){
            //some error happened while copying file
            //update db with error
            imageBuilder.status(Constants.STATUS_IMAGE_PROCESS_ERROR);
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch (Utils.OutofStorageError e){
            //disk full
            //update proper error
            imageBuilder.status(Constants.STATUS_IMAGE_PROCESS_ERROR);
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        } catch (Exception e){
            //Any other exception
            imageBuilder.status(Constants.STATUS_IMAGE_PROCESS_ERROR);
            //contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, Constants.STATUS_IMAGE_PROCESS_ERROR);
        }
        finally {

            //SQLiteDatabase db = DBHelper.getInstance(mContext).getWritableDatabase();
            //imageId = mImageInfo.getImageId() cannot be used because SQLite thinks that 'I12787284y80' is actually a column name, rather than a string/text literal.
            //So we have to wrap it to look like 'I12787284y80'
            RealmContentProvider.updateImage(imageBuilder.id, imageBuilder);
            //db.update(DatabaseContract.ImageBoard.TABLE_NAME, contentValues, DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID+" = "+"'"+mImage.getImageId()+"'", null);

        }
    return null;
    }
}
