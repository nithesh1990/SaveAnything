package napps.saveanything.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;

import io.realm.Realm;
import io.realm.RealmResults;
import napps.saveanything.Model.Builder;
import napps.saveanything.Model.Clip;
import napps.saveanything.R;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.view.Activities.ClipDetailActivity;
import napps.saveanything.view.Activities.DetailClipActivity;
import napps.saveanything.view.customviews.CustomImageView;

/**
 * Created by nithesh on 5/12/2016.
 */
public class ClipListAdapter extends RecyclerRealmAdapter<ClipListAdapter.ClipCardViewHolder, Clip> implements View.OnClickListener{


     private Context mContext;
     public ClipListAdapter(Context context, int layout, RealmResults<Clip> realmResults, BaseSpringSystem baseSpringSystem) {
        super(context, layout, realmResults, baseSpringSystem);
         mContext = context;
      }

    @Override
    public void bindView(ClipCardViewHolder holder, Clip clip, int position) {
        holder.attachedView.setTag(clip);
        holder.favoriteButton.setTag(clip);
        holder.copyButton.setTag(clip);
        holder.moreButton.setTag(clip);
        holder.specialButton.setTag(clip);
        //clipHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT)));
        holder.mainTextView.setText(clip.getContent());
        //String timeText = getTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP)));
        String timeText = getTimeText(clip.getTimeStamp());
        holder.timeTextView.setText(timeText);
        setFavoriteIcon(holder.favoriteButton, clip.isFavorite());

        //String sourcePackage = cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE));
        String sourcePackage = clip.getSourcePackage();
        try{
            Drawable iconDrawable = mContext.getPackageManager().getApplicationIcon(sourcePackage);
            holder.sourceImageView.setImageDrawable(iconDrawable);
        } catch(PackageManager.NameNotFoundException e){
            //TODO: set application not found icon
            e.printStackTrace();
        }
        //long ct = System.currentTimeMillis();
        //String uuid = UUID.randomUUID().toString();
        //clipHolder.timeTextView.setText(uuid);
    }

    @Override
    public ClipCardViewHolder newView(ViewGroup parent, Context context, View view) {
        //first we have to inflate the layoutfile into a view which will convert layout into viewgroup and
        //we can find layout elements by using findviewbyid
         return new ClipCardViewHolder(view);
    }

    @Override
    public boolean changeFavorite(CustomImageView favoriteIcon) {
        Clip clip = (Clip)favoriteIcon.getTag();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        clip.setFavorite(!clip.isFavorite());
        Log.d("ClipAdapter", "Toggling Favorite of Clip: "+clip.getContent());
        realm.copyToRealmOrUpdate(clip);
        realm.commitTransaction();
        return clip.isFavorite();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Clip clip = (Clip)view.getTag();
        switch (id) {
            case R.id.text_card :
                Intent detailIntent = new Intent(mContext, DetailClipActivity.class);
                detailIntent.putExtra(DetailClipActivity.EXTRA_CLIP_ID, clip.getId());
                mContext.startActivity(detailIntent);
                break;
            case R.id.cc_copy_button:
                break;
            case R.id.cc_more_button:
                break;
            case R.id.cc_special_button:
                break;
        }
    }


    public class ClipCardViewHolder extends RecyclerView.ViewHolder {

        View attachedView;
        ImageView sourceImageView;
        TextView mainTextView;
        TextView timeTextView;
        CustomImageView moreButton;
        CustomImageView copyButton;
        CustomImageView favoriteButton;
        CustomImageView specialButton;


        public ClipCardViewHolder(View itemView) {
            super(itemView);
            attachedView = itemView.findViewById(R.id.text_card);
            attachedView.setOnClickListener(ClipListAdapter.this);
            this.sourceImageView = (ImageView) itemView.findViewById(R.id.cc_source_icon);
            this.mainTextView = (TextView) itemView.findViewById(R.id.cc_main_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.cc_time_text);
            //int iconColor = getIconColorFilter(R.styleable.CustomIconColors_iconColorNormal);

            this.copyButton = (CustomImageView) itemView.findViewById(R.id.cc_copy_button);
            this.copyButton.setOnClickListener(ClipListAdapter.this);
            //this.copyButton.setColorFilter(iconColor);
            this.moreButton = (CustomImageView) itemView.findViewById(R.id.cc_more_button);
            this.moreButton.setOnClickListener(ClipListAdapter.this);
            //this.moreButton.setColorFilter(iconColor);
            this.favoriteButton = (CustomImageView) itemView.findViewById(R.id.cc_favorite_button);
            this.favoriteButton.setOnTouchListener(ClipListAdapter.this);
            //this.favoriteButton.setColorFilter(iconColor);
            this.specialButton = (CustomImageView) itemView.findViewById(R.id.cc_special_button);
            this.specialButton.setOnClickListener(ClipListAdapter.this);
            //this.specialButton.setColorFilter(iconColor);

        }
    }

}
