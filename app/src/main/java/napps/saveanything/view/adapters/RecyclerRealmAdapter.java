package napps.saveanything.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;

import io.realm.RealmObject;
import io.realm.RealmResults;
import napps.saveanything.R;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.view.customviews.CustomImageView;

/**
 * Created by nithesh on 5/12/2016.
 */
public abstract class RecyclerRealmAdapter<V extends RecyclerView.ViewHolder, T extends RealmObject> extends RecyclerView.Adapter<V> implements View.OnTouchListener, SpringListener{

    /*This object is generic and can be used for both clips or images. T should be made that extends Realmobject to avoid usage of other objects */
    public RealmResults<T> mRealmResults;
    public Context mContext;
    public int mLayoutResource;
    public LayoutInflater mLayoutInflater;

    public CustomImageView mBouncyView;
    public Spring mScaleSPring;

    public RecyclerRealmAdapter(Context context, int layout, RealmResults<T> realmResults, BaseSpringSystem baseSpringSystem){
        mContext = context;
        mRealmResults = realmResults;
        mLayoutResource = layout;
        mScaleSPring = baseSpringSystem.createSpring();
        mScaleSPring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(Constants.SPRING_FAV_ICON_TENSION, Constants.SPRING_FAV_ICON_FRICTION));
        mScaleSPring.addListener(this);
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

    public abstract boolean changeFavorite(CustomImageView favoriteIcon);

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

    public void setFavoriteIcon(CustomImageView image, boolean isFavorite){
        //We could have used ternary operators here but what i feel is using ternary operators is best for mathematical exressions.
        //It is a bad idea to use ternary operators to replace if else statements. It reduces readability of the code
        //Technically there is no performance difference between ternary operators and if else statements
        if(isFavorite){
            image.setImageResource(R.drawable.ic_favorite_checked_24dp_vector);
        } else {
            image.setImageResource(R.drawable.ic_favorite_unchecked_grey_700_24dp_vector);
        }

    }

    /*
        The implementation of touchListener and SpringListener in super class is a very great design.
        1. Avoids multiple Spring instance creation
        2. It just takes the reference of view to animate at runtime instead of holding it all time
        3. We can add spring animation to any view in child class using just 1 line
            view.setOntouchlistener and few lines change in code
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mBouncyView = (CustomImageView) view;
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                mScaleSPring.setEndValue(Constants.SPRING_SCALE_END_DEFAULT);
                boolean setFavoriteResult = changeFavorite(mBouncyView);
                setFavoriteIcon(mBouncyView, setFavoriteResult);
                Log.d("Spring animation", " Touch action up");
                break;

            case MotionEvent.ACTION_DOWN:
                mScaleSPring.setEndValue(Constants.SPRING_SCALE_START);
                Log.d("Spring animation", " Touch action down");
                break;

            case MotionEvent.ACTION_CANCEL:
                mScaleSPring.setEndValue(Constants.SPRING_SCALE_END_DEFAULT);
                Log.d("Spring animation", " Touch action cancel");

                break;
        }
        return true;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        Log.d("Spring animation", "value: "+String.valueOf(value));
        float scale = 1f - (value * 0.5f);
        mBouncyView.setScaleX(value);
        Log.d("Spring animation", "scale: "+String.valueOf(scale));
        mBouncyView.setScaleY(value);
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    //We did not create a class like below because of possible deadlock as shown below
    //http://stackoverflow.com/questions/26200301/is-not-accessible-in-current-context
    /*public abstract class CommonViewHolder extends RecyclerView.ViewHolder {

        View attachedView;

        public CommonViewHolder(View itemView) {
            super(itemView);
            attachedView = itemView;
        }
    }*/


}
