package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.Utilities.Constants;

/**
 * Created by nithesh on 5/12/2016.
 */
public abstract class RecyclerCursorAdapter<ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {

    public Cursor mCursor;
    public Context mContext;
    public int mLayoutResource;
    public LayoutInflater mLayoutInflater;
    public RecyclerCursorAdapter(Context context, int layout, Cursor cursor){
        mContext = context;
        mCursor = cursor;
        mLayoutResource = layout;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(mLayoutResource, parent, false);
        return newView(parent, mContext, mCursor, view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            Log.d(Constants.APPLICATION_TAG, "On bind view called for position "+position);
            bindView(holder, mCursor, position);
    }

    public abstract void bindView(ViewHolder holder, Cursor cursor, int position);

    public abstract ViewHolder newView(ViewGroup parent, Context context, Cursor mCursor, View view);

    @Override
    public int getItemCount() {

        return mCursor.getCount();

    }

}
