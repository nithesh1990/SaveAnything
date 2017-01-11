package napps.saveanything.view.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.RealmResults;
import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.Model.Clip;
import napps.saveanything.R;
import napps.saveanything.view.customviews.CustomImageView;

/**
 * Created by nithesh on 5/12/2016.
 */
public class ClipListAdapter extends RecyclerRealmAdapter<ClipListAdapter.ClipCardViewHolder, Clip> {

     private Context mContext;
     public ClipListAdapter(Context context, int layout, RealmResults<Clip> realmResults) {
        super(context, layout, realmResults);
         mContext = context;
    }

    @Override
    public void bindView(ClipCardViewHolder holder, Clip clip, int position) {
        ClipCardViewHolder clipHolder = (ClipCardViewHolder) holder;
        //clipHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT)));
        clipHolder.mainTextView.setText(clip.getContent());
        //String timeText = getTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP)));
        String timeText = getTimeText(clip.getTimeStamp());
        clipHolder.timeTextView.setText(timeText);
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
         return new ClipCardViewHolder(view, mContext);
    }

    public class ClipCardViewHolder extends RecyclerView.ViewHolder {

        Context context;
        ImageView sourceImageView;
        TextView mainTextView;
        TextView timeTextView;
        CustomImageView moreButton;
        CustomImageView copyButton;
        CustomImageView favoriteButton;
        CustomImageView specialButton;


        public ClipCardViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            this.sourceImageView = (ImageView) itemView.findViewById(R.id.cc_source_icon);
            this.mainTextView = (TextView) itemView.findViewById(R.id.cc_main_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.cc_time_text);
            //int iconColor = getIconColorFilter(R.styleable.CustomIconColors_iconColorNormal);

            this.copyButton = (CustomImageView) itemView.findViewById(R.id.cc_copy_button);
            //this.copyButton.setColorFilter(iconColor);
            this.moreButton = (CustomImageView) itemView.findViewById(R.id.cc_more_button);
            //this.moreButton.setColorFilter(iconColor);
            this.favoriteButton = (CustomImageView) itemView.findViewById(R.id.cc_favorite_button);
            //this.favoriteButton.setColorFilter(iconColor);
            this.specialButton = (CustomImageView) itemView.findViewById(R.id.cc_special_button);
            //this.specialButton.setColorFilter(iconColor);

        }
    }

}
