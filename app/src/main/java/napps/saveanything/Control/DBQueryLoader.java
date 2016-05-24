package napps.saveanything.Control;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import napps.saveanything.Database.DBContentProvider;
import napps.saveanything.Database.DBHelper;

/**
 * Created by nithesh on 5/16/2016.
 */
public class DBQueryLoader extends AsyncTaskLoader<Cursor> {

    public int mLoaderId;
    private Context mContext;
    private int mSortType;
    public static final int QUERY_ALL_CLIPS = 0;
    public static final int QUERY_ALL_IMAGES = 1;


    public DBQueryLoader(Context context, int loaderId, int sortType) {
        super(context);
        mContext = context;
        mLoaderId = loaderId;
        mSortType = sortType;
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
