package napps.saveanything.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import napps.saveanything.Model.ClipInfo;
import napps.saveanything.Utilities.Constants;

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
 /*
    This link has change
     */

    /*
    This class also represents an Entity Relationship diagram and every application shoulc have a ER diagram of the database.
    If that's not there, then it's a bad design. Especially in applications like this which mainly depend on localization of data
    a well designed schema is more then essential
     */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    private static DBHelper sInstance;
    //Database Version

    //For Every version there is an int
    private final static int DATABASE_VERSION_1 = 1;

    private final static int DATABASE_VERSION_2 = 2;

    private final static int DATABASE_VERSION_3 = 3;

    private final static int DATABASE_VERSION_4 = 4;


    public static final int DATABASE_VERSION = DATABASE_VERSION_2;

    //Database Name
    public static final String DATABASE_NAME = "saveAnyting";

    //Full text version
    public static final String FTS_VERSION = "fts4";

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

    //We don't need type constraints as sql and fts completely ignores types for virtual table
    private static final String SQL_CREATE_VIRTUAL_CLIP_TABLE = "CREATE VIRTUAL TABLE "+DatabaseContract.ClipBoard.TABLE_NAME+
            " USING "+FTS_VERSION+
            OPEN_BRACE+
            DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID + COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE + COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_CLIPSTATUS + COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_CONTENTTYPE + COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT + COMMA_SEPARATOR+
            DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP +
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
        this.context = context;
    }

    //This is called when the app is freshly installed

    //This should be updated accordingly to the new versions
    //When users freshly install the app after 4 versions they should be able to create
    //the database/tables designed for latest version
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_VIRTUAL_CLIP_TABLE);
        db.execSQL(SQL_CREATE_IMAGE_TABLE);
    }

    //This is called when the app is upgraded and database version is not the same i.e when google play performs
    //update and launches the app this is called if the db version is changed
    //Here we have to maintain consistency. If there are successive upgrades not all users update every version of the app
    //Users may upgrade from version 1 to 4 directly while some upgrade from 3 to 4
    @Override
    public void onUpgrade(SQLiteDatabase updatedDB, int oldVersion, int newVersion) {

        //ClipInfo always represents a clip record. This clipInfo might get updated from version to version
        //This also acts as a data holder for transferring data from older tables to new tables
        //Transferring data from table to table using columns might create problems and if some error happens like type mismatch etc
        //then whole data will be lost which is very bad.
        //Make sure to run this in a separate non killable service in the background so that when user opens it won't take very long time
        //

        ArrayList<ClipInfo> oldClipsList = new ArrayList<>();
        switch (oldVersion){
            //For every old version add a case statement to extract the data into
            case DATABASE_VERSION_1:
                //TO insert the data as it was inserted in the original table we have to fetch from oldest ones and come to latest ones
                //So this sort is made ascending

                //We can't get values like this because this is a class that has methods OnCreate and Onupgrade which will create and upgrade the database.
                //During this time the database will not be available. Any attempt to call getReadeableDatabase will in turn call Oncreate database which makes a recursive call
                //and hence app crashes
                Cursor cursor = null;
                //Cursor cursor = DBContentProvider.getAllClipsforDisplay(getInstance(context), Constants.SORT_TIME_OLD_FIRST);
                //So we have to use the db instance that is provided in this method argument and not touch any outside methods
                    cursor = DBContentProvider.getAllClipsforDisplay(updatedDB, Constants.SORT_TIME_OLD_FIRST);
                if(cursor != null && cursor.getCount() > 0){
                    //Here do while is used because cursor is already move to first and we don't have hasNext() to check next available data
                    //We only have moveToNext()
                    cursor.moveToFirst();
                    do {
                        ClipInfo.Builder clipBuilder = new ClipInfo.Builder();
                        if(cursor.getColumnIndex(DatabaseContract.ClipBoard._ID) != -1){
                            clipBuilder.Id(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ClipBoard._ID)));
                        }
                        if(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID) != -1){
                            clipBuilder.ClipId(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CLIPID)));
                        }
                        if(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT) != -1){
                            clipBuilder.Content(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT)));
                        }
                        if(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENTTYPE) != -1){
                            clipBuilder.ContentType(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENTTYPE)));
                        }
                        if(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE) != -1){
                            clipBuilder.SourcePackage(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE)));
                        }
                        if(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP) != -1){
                            clipBuilder.Timestamp(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP)));
                        }
                        if(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CLIPSTATUS) != -1){
                            clipBuilder.ClipStatus(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CLIPSTATUS)));
                        }
                        // we can also add some default values to the new columns that are not present in old tables
                        oldClipsList.add(clipBuilder.build());

                    } while(cursor.moveToNext());
                }
                //TODO:Since we have copied the data we will drop this table. We have to be careful here as the whole data is held in an intermediate arraylist
                //So we have to take care of scenarios where user suddenly closes app, phone shutdown due to battery low, out of memory errors. All the coped data will be lost.
                //Now we will drop the table before creating a table with the same name. We use 'if exists' an user might have just installed the app and before opening he recieves
                //an update and directly opens v2 for the first time. In that case v1 table would not have been created
                updatedDB.execSQL(SQL_DELETE_CLIP_TABLE);
                break;
        }

        switch(newVersion){
            //It is redundant to add each case for a version because user will always update it to latest version from different older versions
            //Still in some case user can't install from playstore and installs from a .apk which has intermediate version, we have to support that.
            //V E   R   Y           I   M   P   O   R   T   A   N   T
            //This design is a MANY-ONE-MANY Design which supports database upgrade from any version to any version and even downgrade if this is called upon downgrade.
            //We can give the user an option to downgrade to previous versions(first time ever feature) without losing data
            case DATABASE_VERSION_2:
                updatedDB.execSQL(SQL_CREATE_VIRTUAL_CLIP_TABLE);
                if(oldClipsList.size() > 0){
                    for (ClipInfo clip:
                         oldClipsList) {
                        //This internally takes care of checking each value before inserting
                        DBContentProvider.insertClip(updatedDB, clip);
                    }
                }
        }

//        db.execSQL(SQL_DELETE_IMAGE_TABLE);
//        db.execSQL(SQL_CREATE_CLIP_TABLE);
//        db.execSQL(SQL_CREATE_IMAGE_TABLE);
    }
}
