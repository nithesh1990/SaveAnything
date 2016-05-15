package napps.saveanything.Database;

import android.content.ContentValues;
import android.content.Context;

import napps.saveanything.Model.ClipInfo;
import napps.saveanything.Model.ImageInfo;

/**
 * Created by nithesh on 5/14/2016.
 */
public class DBContentProvider {
    private DBHelper mDBHelper;


    public DBContentProvider(Context context) {
        this.mDBHelper = DBHelper.getInstance(context);
    }

    public boolean insertClip(ClipInfo clipInfo){
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
            return mDBHelper.getWritableDatabase().insert(DatabaseContract.ClipBoard.TABLE_NAME, null, contentValues) > 0 ? true : false;
        } else {
            return false;
        }

    }

    public boolean insertImage(ImageInfo imageInfo){
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
            return mDBHelper.getWritableDatabase().insert(DatabaseContract.ImageBoard.TABLE_NAME, null, contentValues) > 0 ? true : false;
        } else {
            return false;
        }

    }
}
