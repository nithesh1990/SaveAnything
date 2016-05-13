package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import napps.saveanything.R;

/**
 * Created by nithesh on 5/12/2016.
 */
public class TextListAdapter extends RecyclerCursorAdapter {

    public LayoutInflater mLayoutInflator;

    public TextListAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor);
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, Cursor cursor) {
        TextViewHolder textHolder = (TextViewHolder) holder;
        textHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex("main_text")));
        textHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex("time_text")));
    }

    @Override
    public RecyclerView.ViewHolder newView(ViewGroup parent, Context context, Cursor cursor, View view) {
        //first we have to inflate the layoutfile into a view which will convert layout into viewgroup and
        //we can find layout elements by using findviewbyid
         return new TextViewHolder(view);
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {

        public TextView mainTextView;
        public TextView timeTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.mainTextView = (TextView) itemView.findViewById(R.id.main_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        }
    }
}
