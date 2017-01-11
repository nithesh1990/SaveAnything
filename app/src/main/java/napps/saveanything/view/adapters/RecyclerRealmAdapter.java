package napps.saveanything.view.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import io.realm.RealmObject;
import io.realm.RealmResults;
import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.Utilities.Constants;

/**
 * Created by nithesh on 5/12/2016.
 */
public abstract class RecyclerRealmAdapter<V extends RecyclerView.ViewHolder, T extends RealmObject> extends RecyclerView.Adapter<V> {

    /*This object is generic and can be used for both clips or images. T should be made that extends Realmobject to avoid usage of other objects */
    public RealmResults<T> mRealmResults;
    public Context mContext;
    public int mLayoutResource;
    public LayoutInflater mLayoutInflater;
    public RecyclerRealmAdapter(Context context, int layout, RealmResults<T> realmResults){
        mContext = context;
        mRealmResults = realmResults;
        mLayoutResource = layout;

    }
    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(mLayoutResource, parent, false);
        return newView(parent, mContext, view);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
            Log.d(Constants.APPLICATION_TAG, "On bind view called for position "+position);
            bindView(holder, mRealmResults.get(position), position);
    }

    public abstract void bindView(V holder, T realmObject, int position);

    public abstract V newView(ViewGroup parent, Context context, View view);

    @Override
    public int getItemCount() {

        return mRealmResults.size();

    }

    public String getTimeText(long actualmilliSeconds){
        String timeText = "";
        long currentmillis = System.currentTimeMillis();
        long secondsPassed = (currentmillis - actualmilliSeconds);

        if(secondsPassed >= Constants.SECOND && secondsPassed < Constants.MINUTE){
            long seconds = secondsPassed/Constants.SECOND;
            timeText = String.valueOf(seconds)+" seconds "+Constants.TIME_PASSED;
        } else if(secondsPassed >= Constants.MINUTE && secondsPassed < Constants.HOUR){
            long minutes = secondsPassed/Constants.MINUTE;
            timeText = String.valueOf(minutes)+" minutes "+Constants.TIME_PASSED;
        } else if(secondsPassed >= Constants.HOUR && secondsPassed < Constants. DAY){
            long hours = secondsPassed/Constants.HOUR;
            timeText = String.valueOf(hours)+" hours "+Constants.TIME_PASSED;
        } else if(secondsPassed >= Constants.DAY && secondsPassed < Constants.WEEK){
            long days = secondsPassed/Constants.DAY;
            timeText = String.valueOf(days)+" days "+Constants.TIME_PASSED;
        } else if(secondsPassed >= Constants.WEEK && secondsPassed < Constants.MONTH){
            long weeks = secondsPassed/Constants.WEEK;
            timeText = String.valueOf(weeks)+" weeks "+Constants.TIME_PASSED;
        } else if(secondsPassed >= Constants.MONTH && secondsPassed < Constants.YEAR) {
            long months = secondsPassed/Constants.MONTH;
            timeText = String.valueOf(months)+" months "+Constants.TIME_PASSED;
        }

        return timeText;
    }



}
