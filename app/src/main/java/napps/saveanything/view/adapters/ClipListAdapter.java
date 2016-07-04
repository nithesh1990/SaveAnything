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

import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.R;

/**
 * Created by nithesh on 5/12/2016.
 */
public class ClipListAdapter extends RecyclerCursorAdapter {

     private Context mContext;
     public ClipListAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor);
         mContext = context;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, Cursor cursor, int position) {
        ClipCardViewHolder clipHolder = (ClipCardViewHolder) holder;
        clipHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT)));

        String timeText = getTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP)));
        clipHolder.timeTextView.setText(timeText);
        String sourcePackage = cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE));
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
    public RecyclerView.ViewHolder newView(ViewGroup parent, Context context, Cursor cursor, View view) {
        //first we have to inflate the layoutfile into a view which will convert layout into viewgroup and
        //we can find layout elements by using findviewbyid
         return new ClipCardViewHolder(view);
    }

    private class ClipCardViewHolder extends RecyclerView.ViewHolder {

        ImageView sourceImageView;
        TextView mainTextView;
        TextView timeTextView;

        public ClipCardViewHolder(View itemView) {
            super(itemView);
            this.sourceImageView = (ImageView) itemView.findViewById(R.id.cc_source_icon);
            this.mainTextView = (TextView) itemView.findViewById(R.id.cc_main_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.cc_time_text);
        }
    }
}
