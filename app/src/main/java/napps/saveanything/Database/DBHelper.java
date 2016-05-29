package napps.saveanything.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nithesh on 5/14/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;
    //Database Version
    public static final int DATABASE_VERSION = 1;

    //Database Name
    public static final String DATABASE_NAME = "saveAnyting";

    private static final String SQL_CREATE_CLIP_TABLE = "CREATE TABLE "+DatabaseContract.ClipBoard.TABLE_NAME +" ( "+
            DatabaseContract.ClipBoard._ID +" INTEGER PRIMARY KEY "+","+
            DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID+" TEXT "+","+
            DatabaseContract.ClipBoard.COLUMN_NAME_TITLE+" TEXT "+","+
            DatabaseContract.ClipBoard.COLUMN_NAME_CONTENTTYPE+" INTEGER "+","+
            DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT+" TEXT "+","+
            DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP+" INTEGER "+
            " )";

    private static final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE "+DatabaseContract.ImageBoard.TABLE_NAME +" ( "+
            DatabaseContract.ImageBoard._ID +" INTEGER PRIMARY KEY "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID+" TEXT "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_TITLE+" TEXT "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_ORIGINALPATH+" TEXT "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP+" INTEGER "+
            DatabaseContract.ImageBoard.COLUMN_NAME_STATUS+" INTEGER "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH+" INTEGER "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_IS_SCALED+" INTEGER "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_FACTOR+" INTEGER "+","+
            DatabaseContract.ImageBoard.COLUMN_NAME_IMAGESIZE+" INTEGER "+","+
            " )";

    private static final String SQL_DELETE_CLIP_TABLE = "DROP TABLE IF EXISTS "+DatabaseContract.ClipBoard.TABLE_NAME;
    private static final String SQL_DELETE_IMAGE_TABLE = "DROP TABLE IF EXISTS "+DatabaseContract.ImageBoard.TABLE_NAME;

    public static synchronized DBHelper getInstance(Context context){

        if(sInstance == null){
            sInstance = new DBHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLIP_TABLE);
        db.execSQL(SQL_CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CLIP_TABLE);
        db.execSQL(SQL_DELETE_IMAGE_TABLE);

    }
}
