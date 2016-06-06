package napps.saveanything.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nithesh on 5/14/2016.
 */
/*
    Singleton Pattern Reason :
        The SqliteOpenHelper object holds on to one database connection. It appears to offer you a read and write connection, but it really doesn't. Call the read-only, and you'll get the write database connection regardless.

        So, one helper instance, one db connection. Even if you use it from multiple threads, one connection at a time. The SqliteDatabase object uses java locks to keep access serialized. So, if 100 threads have one db instance, calls to the actual on-disk database are serialized.

        So, one helper, one db connection, which is serialized in java code. One thread, 1000 threads, if you use one helper instance shared between them, all of your db access code is serial. And life is good (ish).

        If you try to write to the database from actual distinct connections at the same time, one will fail. It will not wait till the first is done and then write. It will simply not write your change. Worse, if you don’t call the right version of insert/update on the SQLiteDatabase, you won’t get an exception. You’ll just get a message in your LogCat, and that will be it.

        So, multiple threads? Use one helper. Period. If you KNOW only one thread will be writing, you MAY be able to use multiple connections, and your reads will be faster, but buyer beware. I haven't tested that much.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;
    //Database Version
    public static final int DATABASE_VERSION = 1;

    //Database Name
    public static final String DATABASE_NAME = "saveAnyting";

    private static final String COMMA_SEPARATOR = ",";
    private static final String OPEN_BRACE = " ( ";
    private static final String CLOSE_BRACE = " ) ";

    private static final String SQL_CREATE_CLIP_TABLE = "CREATE TABLE "+DatabaseContract.ClipBoard.TABLE_NAME +
            OPEN_BRACE+
            DatabaseContract.ClipBoard._ID +" INTEGER PRIMARY KEY "+COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID+" TEXT "+COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE+" TEXT "+COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_CONTENTTYPE+" INTEGER "+COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT+" TEXT "+COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP+" INTEGER "+
            CLOSE_BRACE;

    private static final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE "+DatabaseContract.ImageBoard.TABLE_NAME +
            OPEN_BRACE+
            DatabaseContract.ImageBoard._ID +" INTEGER PRIMARY KEY "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID+" TEXT "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_DESC+" TEXT "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_ORIGINALPATH+" TEXT "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP+" INTEGER "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_STATUS+" INTEGER "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH+" INTEGER "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_STATUS+" INTEGER "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_SCALE_FACTOR+" INTEGER "+COMMA_SEPARATOR+
            DatabaseContract.ImageBoard.COLUMN_NAME_IMAGESIZE+" INTEGER "+
            CLOSE_BRACE;

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

    //This is called when the app is freshly installed
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLIP_TABLE);
        db.execSQL(SQL_CREATE_IMAGE_TABLE);
    }

    //This is called when the app is upgraded and database version is not the same i.e when google play performs
    //update and launches the app this is called if the db version is changed
    //Here we have to maintain consistency. If there are successive upgrades not all users update every version of the app
    //Users may upgrade from version 1 to 4 directly while some upgrade from 3 to 4
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_CLIP_TABLE);
        db.execSQL(SQL_DELETE_IMAGE_TABLE);
        db.execSQL(SQL_CREATE_CLIP_TABLE);
        db.execSQL(SQL_CREATE_IMAGE_TABLE);
    }
}
