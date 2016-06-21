package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.UUID;

import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.R;

/**
 * Created by nithesh on 5/12/2016.
 */
public class TextListAdapter extends RecyclerCursorAdapter {

     private Context mContext;
     public TextListAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor);
         mContext = context;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, Cursor cursor, int position) {
        TextCardViewHolder textHolder = (TextCardViewHolder) holder;
        textHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT)));
        textHolder.timeTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP)));
        //long ct = System.currentTimeMillis();
        //String uuid = UUID.randomUUID().toString();
        //textHolder.timeTextView.setText(uuid);
    }

    @Override
    public RecyclerView.ViewHolder newView(ViewGroup parent, Context context, Cursor cursor, View view) {
        //first we have to inflate the layoutfile into a view which will convert layout into viewgroup and
        //we can find layout elements by using findviewbyid
         return new TextCardViewHolder(view);
    }

    private class TextCardViewHolder extends RecyclerView.ViewHolder {

        public TextView mainTextView;
        public TextView timeTextView;

        public TextCardViewHolder(View itemView) {
            super(itemView);
            this.mainTextView = (TextView) itemView.findViewById(R.id.main_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        }
    }
}
