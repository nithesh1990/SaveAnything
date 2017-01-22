package napps.saveanything.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import napps.saveanything.Model.Builder;
import napps.saveanything.Model.Clip;
import napps.saveanything.R;
import napps.saveanything.view.Activities.ClipDetailActivity;
import napps.saveanything.view.customviews.CustomImageView;

/**
 * Created by nithesh on 5/12/2016.
 */
public class ClipListAdapter extends RecyclerRealmAdapter<ClipListAdapter.ClipCardViewHolder, Clip> implements View.OnClickListener {

     private Context mContext;
     public ClipListAdapter(Context context, int layout, RealmResults<Clip> realmResults) {
        super(context, layout, realmResults);
         mContext = context;
    }

    @Override
    public void bindView(ClipCardViewHolder holder, Clip clip, int position) {
        ClipCardViewHolder clipHolder = (ClipCardViewHolder) holder;
        holder.attachedView.setTag(clip);
        holder.favoriteButton.setTag(clip);
        holder.copyButton.setTag(clip);
        holder.moreButton.setTag(clip);
        holder.specialButton.setTag(clip);
        //clipHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT)));
        clipHolder.mainTextView.setText(clip.getContent());
        //String timeText = getTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP)));
        String timeText = getTimeText(clip.getTimeStamp());
        clipHolder.timeTextView.setText(timeText);
        if(clip.isFavorite()){
            clipHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_checked_24dp_vector);
            Log.d("ClipAdapter", "Favorite Clip: "+clip.getContent());
        }

        //String sourcePackage = cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE));
        String sourcePackage = clip.getSourcePackage();
        try{
            Drawable iconDrawable = mContext.getPackageManager().getApplicationIcon(sourcePackage);
            clipHolder.sourceImageView.setImageDrawable(iconDrawable);
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
    public void onClick(View view) {
        int id = view.getId();
        Clip clip = (Clip)view.getTag();
        switch (id) {
            case R.id.text_card :
                Intent detailIntent = new Intent(mContext, ClipDetailActivity.class);
                detailIntent.putExtra("ClipId", clip.getId());
                mContext.startActivity(detailIntent);
                break;
            case R.id.cc_copy_button:
                break;
            case R.id.cc_favorite_button:
                //We could have called updateclip method in realmContentprovider but that's not an efficient method as
                //it updates each and every value which is not necessary. We should actually remove that update method as it is inefficient althought it looks technically beautiful
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                clip.setFavorite(!clip.isFavorite());
                Log.d("ClipAdapter", "Toggling Favorite of Clip: "+clip.getContent());
                realm.copyToRealmOrUpdate(clip);
                realm.commitTransaction();
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
            attachedView = itemView;
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
            this.favoriteButton.setOnClickListener(ClipListAdapter.this);
            //this.favoriteButton.setColorFilter(iconColor);
            this.specialButton = (CustomImageView) itemView.findViewById(R.id.cc_special_button);
            this.specialButton.setOnClickListener(ClipListAdapter.this);
            //this.specialButton.setColorFilter(iconColor);

        }
    }

}
