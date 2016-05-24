package napps.saveanything.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import napps.saveanything.Data.Constants;
import napps.saveanything.Model.ClipInfo;
import napps.saveanything.Model.ImageInfo;

/**
 * Created by nithesh on 5/14/2016.
 */
public class DBContentProvider {

    private static final String SORT_ORDER_ASC = " ASC ";
    private static final String SORT_ORDER_DESC = " DESC ";


    public DBContentProvider(Context context) {
    }

    public static synchronized boolean insertClip(DBHelper dbHelper, ClipInfo clipInfo){
        if(clipInfo == null){
            return false;
        }
        ContentValues contentValues = new ContentValues();
        if(clipInfo.getClipId() != null && !clipInfo.getClipId().isEmpty()){
            contentValues.put(DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID, clipInfo.getClipId());
        }
        if(clipInfo.getTitle() != null && !clipInfo.getTitle().isEmpty()){
            contentValues.put(DatabaseContract.ClipBoard.COLUMN_NAME_TITLE, clipInfo.getTitle());
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

    public static synchronized boolean insertImage(DBHelper dbHelper, ImageInfo imageInfo){
        if(imageInfo == null){
            return false;
        }
        ContentValues contentValues = new ContentValues();
        if(imageInfo.getImageId() != null && !imageInfo.getImageId().isEmpty()){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID, imageInfo.getImageId());
        }
        if(imageInfo.getTitle() != null && !imageInfo.getTitle().isEmpty()){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_TITLE, imageInfo.getTitle());
        }
        if(imageInfo.getImagePath() != null && !imageInfo.getImagePath().isEmpty()){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEPATH, imageInfo.getImagePath());
        }
        if(imageInfo.getContentType() > 0 ){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_CONTENTTYPE, imageInfo.getContentType());
        }
        if(imageInfo.getTimestamp() > 0 ){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP, imageInfo.getTimestamp());
        }
        if(imageInfo.getImageSize() > 0 ){
            contentValues.put(DatabaseContract.ImageBoard.COLUMN_NAME_IMAGESIZE, imageInfo.getImageSize());
        }
        if(contentValues.size() > 0){
            return dbHelper.getWritableDatabase().insert(DatabaseContract.ImageBoard.TABLE_NAME, null, contentValues) > 0 ? true : false;
        } else {
            return false;
        }

    }

    public static synchronized Cursor getAllClipsforDisplay(DBHelper dbHelper, int sortType){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.ClipBoard._ID,
                DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID,
                DatabaseContract.ClipBoard.COLUMN_NAME_TITLE,
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
            case Constants.SORT_TITLE_ASCENDING:
                sortOrder = DatabaseContract.ClipBoard.COLUMN_NAME_TITLE +SORT_ORDER_ASC;
                break;
            case Constants.SORT_TITLE_DESCENDING:
                sortOrder = DatabaseContract.ClipBoard.COLUMN_NAME_TITLE +SORT_ORDER_DESC;
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
                DatabaseContract.ImageBoard.COLUMN_NAME_TITLE,
                DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEPATH,
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
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_TITLE +SORT_ORDER_ASC;
                break;
            case Constants.SORT_TITLE_DESCENDING:
                sortOrder = DatabaseContract.ImageBoard.COLUMN_NAME_TITLE +SORT_ORDER_DESC;
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
