package napps.saveanything.Control;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;

import napps.saveanything.Database.DBContentProvider;
import napps.saveanything.Database.DBHelper;

/**
 * Created by nithesh on 5/16/2016.
 */
/*
    Android's CursorLoader also extends AsyncTaskLoader<Cursor>. We could have used that.
    The reason for using a separate Loader is
    1. CursorLoader requires query to be provided in content uri format with projection.
    2. Since we already have a query method written specifically, using a new one doesn't make sense
    3. Moreover we can use any methods to run in the background
    4. If we initialize cursorLoader it simply initializes many variables and objects which we don't require
    5. Here we initialize only required variables and objects
 */
public class DBQueryLoader extends AsyncTaskLoader<Cursor> {

    CursorLoader mLoader;
    public int mLoaderId;
    private Context mContext;
    private int mSortType;
    public static final int QUERY_ALL_CLIPS = 0;
    public static final int QUERY_ALL_IMAGES = 1;


    //This is Dependency Injection Design Pattern
    //Dependency injection is basically providing the objects that an object needs (its dependencies) instead of having it construct them itself.
    //It's a very useful technique for testing, since it allows dependencies to be mocked or stubbed out.
    //Whenever possible pass object instances through constructor or getter setter methods
    //which becomes useful like once you have set all the available instance variables through setters or getters method
    //we can directly call the methods in test classes and do rigorous testing by passing arbitrary values.

    public DBQueryLoader(Context context, int loaderId, int sortType) {
        super(context);
        this.mContext = context;
        this.mLoaderId = loaderId;
        this.mSortType = sortType;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor;

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        switch (mLoaderId){
            case QUERY_ALL_CLIPS:
                cursor = DBContentProvider.getAllClipsforDisplay(dbHelper, mSortType);
                break;
            case QUERY_ALL_IMAGES:
                cursor = DBContentProvider.getAllImagesforDisplay(dbHelper, mSortType);
                break;
            default:
                cursor = null;
        }

        return cursor;
    }

    @Override
    protected Cursor onLoadInBackground() {
        return super.onLoadInBackground();
    }

    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
    }

    @Override
    public boolean isLoadInBackgroundCanceled() {
        return super.isLoadInBackgroundCanceled();
    }
}
