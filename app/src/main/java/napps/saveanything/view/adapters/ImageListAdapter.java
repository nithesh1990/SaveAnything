package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import napps.saveanything.R;

/**
 * Created by nithesh on 5/13/2016.
 */
public class ImageListAdapter extends RecyclerCursorAdapter {

    public ImageListAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor);
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, Cursor cursor) {
        ImageCardViewHolder imageCardHolder = (ImageCardViewHolder) holder;
        imageCardHolder.titleTextView.setText(R.string.temp_image_title);
        imageCardHolder.timeTextView.setText(R.string.temp_text_time);
        //imageCardHolder.mainImage.is
    }

    @Override
    public RecyclerView.ViewHolder newView(ViewGroup parent, Context context, Cursor mCursor, View view) {
        return new ImageCardViewHolder(view);
    }

    private class ImageCardViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView mainImage;
        public TextView timeTextView;

        public ImageCardViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView.findViewById(R.id.title_image);
            this.mainImage = (ImageView) itemView.findViewById(R.id.main_image);
            this.timeTextView = (TextView) itemView.findViewById(R.id.time_image);
        }
    }
}
