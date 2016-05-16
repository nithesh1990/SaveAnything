package napps.saveanything.view.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by nithesh on 5/6/2016.
 */
public class Fraggment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String Title;
    //Make these public so that it is visible for subclasses
    public static final int QUERY_ALL_CLIPS = 0;
    public static final int QUERY_ALL_IMAGES = 1;


    @Override
    public Loader<Cursor> onCreateLoader(int queryId, Bundle args) {

        switch(queryId){
            case QUERY_ALL_CLIPS:
                return new CursorLoader(getActivity());

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String mTitle) {
        this.Title = mTitle;
    }
}
