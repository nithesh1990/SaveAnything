package napps.saveanything.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.Model.ClipInfo;
import napps.saveanything.Model.ImageInfo;

/**
 * Created by nithesh on 5/14/2016.
 */
public class DBContentProvider {

    //Since this is static final is is instantiated once and not called everytime
    private static final String CLASS_TAG = DBContentProvider.class.getSimpleName();

    private static final String SORT_ORDER_ASC = " ASC ";
    private static final String SORT_ORDER_DESC = " DESC ";


    public DBContentProvider() {
    }

    //The method is made static it doesn't depend on DBContentProvider object creation as we are not accessing any of its instance variables
    //This is a standalone method and it doesn't create lot of problem JVM/DVM internally optimizes static method a lot
    //This is just validation of few things and running a single command(i.e insert) so it is made static. Since this is required at many places
    //creating a common static methods would solve the problem
    //We don't need to synchronize this method because dbHelper.getWritableDatabase()/dbHelper.getReadableDatabase() is already synchronized
    //synchronized inside synchronized reduces degrades performance
    public static boolean insertClip(DBHelper dbHelper, ClipInfo clipInfo){
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "insertClip","Called");
        if(clipInfo == null){
            return false;
        }
        ContentValues contentValues = new ContentValues();
        if(clipInfo.getClipId() != null && !clipInfo.getClipId().isEmpty()){
            contentValues.put(DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID, clipInfo.getClipId());
        }
        if(clipInfo.getSourcePackage() != null && !clipInfo.getSourcePackage().isEmpty()){
            contentValues.put(DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE, clipInfo.getSourcePackage());
        }
        if(clipInfo.getContent() != null && !clipInfo.getContent().isEmpty()){
            contentValues.put(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT, clipInfo.getContent());
        }
        if(clipInfo.getContentType() != 0 ){
            contentValues.put(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENTTYPE, clipInfo.getContentType());
        }
        if(clipInfo.getTimestamp() != 0 ){
            contentValues.put(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP, clipInfo.getTimestamp());
        }
        if(contentValues.size() > 0){
            return dbHelper.getWritableDatabase().insert(DatabaseContract.ClipBoard.TABLE_NAME, null, contentValues) > 0 ? true : false;
        } else {
            return false;
        }

    }

    //The method is made static it doesn't depend on DBContentProvider object creation as we are not accessing any of its instance variables
    //This is a standalone method and it doesn't create lot of problem JVM/DVM internally optimizes static method a lot
    //This is just validation of few things and running a single command(i.e insert) so it is made static. Since this is required at many places
    //creating a common static methods would solve the problem
    //We don't need to synchronize this method because dbHelper.getWritableDatabase()/dbHelper.getReadableDatabase() is already synchronized
    //synchronized inside synchronized reduces degrades performance

    public static boolean insertImage(DBHelper dbHelper, ImageInfo imageInfo){
        if(imageInfo == null){
            return false;
        }
        ContentValues contentValues = new ContentValues();
        if(imageInfo.getImageId() != null && !imageInfo.getImageId().isEmpty()){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID, imageInfo.getImageId());
        }
        if(imageInfo.getDesc() != null && !imageInfo.getDesc().isEmpty()){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_DESC, imageInfo.getDesc());
        }
        if(imageInfo.getOriginalPath() != null && !imageInfo.getOriginalPath().isEmpty()){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_ORIGINALPATH, imageInfo.getOriginalPath());
        }

        if(imageInfo.getTimestamp() > 0 ){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP, imageInfo.getTimestamp());
        } else {
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP, System.currentTimeMillis());
        }

        if(imageInfo.getImageSize() > 0 ){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_IMAGESIZE, imageInfo.getImageSize());
        }

        if(imageInfo.getSavedPath() != null){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH, imageInfo.getSavedPath());
        }

        contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_STATUS, imageInfo.getStatus());

        if(imageInfo.getScaleStatus() > 0){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS, imageInfo.getScaleStatus());
        }

        if(imageInfo.getScaleFactor() > 0){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_FACTOR, imageInfo.getScaleFactor());
        }

        if(contentValues.size() > 0){
            return dbHelper.getWritableDatabase().insert(DatabaseContract.ImageBoard.TABLE_NAME, null, contentValues) > 0 ? true : false;
        } else {
            return false;
        }

    }

    //The method is made static it doesn't depend on DBContentProvider object creation as we are not accessing any of its instance variables
    //This is a standalone method and it doesn't create lot of problem JVM/DVM internally optimizes static method a lot
    //This is just validation of few things and running a single command(i.e insert) so it is made static. Since this is required at many places
    //creating a common static methods would solve the problem
    //Need to research more on using synchronized keyword because query() doesn't seem to be synchronized internally


    public static synchronized Cursor getAllClipsforDisplay(DBHelper dbHelper, int sortType){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.ClipBoard._ID,
                DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID,
                DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE,
                DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT,
                DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP
        };

        String sortOrder;

        switch(sortType){
            case Constants.SORT_CONTENTS_ASCENDING:
                sortOrder = DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT +SORT_ORDER_ASC;
                break;
            case Constants.SORT_CONTENTS_DESCENDING:
                sortOrder = DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT +SORT_ORDER_DESC;
                break;
            case Constants.SORT_TIME_OLD_FIRST:
                sortOrder = DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP +SORT_ORDER_ASC;
                break;

            case Constants.SORT_TIME_NEW_FIRST:
            case Constants.SORT_DEFAULT:
            default:
                sortOrder = DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP +SORT_ORDER_DESC;
                break;
             }
        Cursor cursor = sqLiteDatabase.query(DatabaseContract.ClipBoard.TABLE_NAME, projection, null, null, null, null, sortOrder);

        return cursor;
    }

    public static synchronized Cursor getAllImagesforDisplay(DBHelper dbHelper, int sortType){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.ImageBoard._ID,
                DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID,
                DatabaseContract.ImageBoard.COLUMN_NAME_DESC,
                DatabaseContract.ImageBoard.COLUMN_NAME_ORIGINALPATH,
                DatabaseContract.ImageBoard.COLUMN_NAME_STATUS,
                DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS,
                DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_FACTOR,
                DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH,
                DatabaseContract.ImageBoard.COLUMN_NAME_IMAGESIZE,
                DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP
        };

        //TODO
        //add clip_status and image_status column for newly added, deleted, starred etc
        String sortOrder;

        switch(sortType){
            case Constants.SORT_MEDIA_SIZE_ASCENDING:
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_IMAGESIZE +SORT_ORDER_ASC;
                break;
            case Constants.SORT_MEDIA_SIZE_DESCENDING:
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_IMAGESIZE +SORT_ORDER_DESC;
                break;
            case Constants.SORT_TITLE_ASCENDING:
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_DESC +SORT_ORDER_ASC;
                break;
            case Constants.SORT_TITLE_DESCENDING:
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_DESC +SORT_ORDER_DESC;
                break;
            case Constants.SORT_TIME_OLD_FIRST:
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP +SORT_ORDER_ASC;
                break;

            case Constants.SORT_TIME_NEW_FIRST:
            case Constants.SORT_DEFAULT:
            default:
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP +SORT_ORDER_DESC;
                break;
        }
        Cursor cursor = sqLiteDatabase.query(DatabaseContract.ImageBoard.TABLE_NAME, projection, null, null, null, null, sortOrder);

        return cursor;
    }

}
