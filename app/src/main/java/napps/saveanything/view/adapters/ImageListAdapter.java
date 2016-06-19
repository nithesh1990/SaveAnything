package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import napps.saveanything.Control.BitmapManager;
import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.R;

/**
 * Created by nithesh on 5/13/2016.
 */
public class ImageListAdapter extends RecyclerCursorAdapter {

    private int deviceHeight;
    private int deviceWidth;
    private BitmapManager bitmapManager;

    public ImageListAdapter(Context context, int layout, Cursor cursor, int deviceWidth, int deviceHeight) {
        super(context, layout, cursor);
        this.deviceHeight = deviceHeight;
        this.deviceWidth = deviceWidth;
        bitmapManager = BitmapManager.getInstance();

    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, Cursor cursor) {
        ImageCardViewHolder imageCardHolder = (ImageCardViewHolder) holder;
        imageCardHolder.titleTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID)));
        imageCardHolder.timeTextView.setText(cursor.getInt(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP)));
        Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH)));
        bitmapManager.setBitmap(imageUri, cursor.getPosition(), imageCardHolder.mainImage, deviceWidth, deviceHeight);
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
