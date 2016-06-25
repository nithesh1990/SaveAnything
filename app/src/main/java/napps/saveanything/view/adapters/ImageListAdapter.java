package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import napps.saveanything.Control.BitmapManager;
import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.R;
import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.view.customviews.CustomTextView;

/**
 * Created by nithesh on 5/13/2016.
 */
public class ImageListAdapter extends RecyclerCursorAdapter {

    private int deviceHeight;
    private int deviceWidth;
    private BitmapManager bitmapManager;
    private GridLayoutManager gridLayoutManager;
    private Context mContext;

    public ImageListAdapter(Context context, RecyclerView currentView, int layout, Cursor cursor, int deviceWidth, int deviceHeight) {
        super(context, layout, cursor);
        this.deviceHeight = deviceHeight;
        this.deviceWidth = deviceWidth;
        mContext = context;
        bitmapManager = BitmapManager.getInstance();
        bitmapManager.initializeBM(context);
        bitmapManager.setBitmapsViewHolder(currentView);

    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, Cursor cursor, int position) {
        ImageCardViewHolder imageCardHolder = (ImageCardViewHolder) holder;
        imageCardHolder.titleTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_IMAGEID)));
        String message = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_DESC));
        String displayMsg = String.valueOf(position)+". "+message;
        imageCardHolder.descTextView.setText(displayMsg);
        AppLogger.addLogMessage(AppLogger.DEBUG, ImageListAdapter.class.getSimpleName(), "bindView()", "ImageListAdapter bind view called for position "+cursor.getPosition());
        try{
            //Sometimes error occurs while saving the image and image path is not saved properly
            //TODO: Have to handle that case
            String imageUri = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH));
            //Glide.with(mContext).load(imageUri).placeholder(R.drawable.image_loading).into(imageCardHolder.mainImage);
            bitmapManager.setBitmap(imageUri, position, imageCardHolder.mainImage, deviceWidth, deviceHeight);

        }catch(Exception e){
            e.printStackTrace();
        }
         //imageCardHolder.mainImage.is
    }

    @Override
    public RecyclerView.ViewHolder newView(ViewGroup parent, Context context, Cursor mCursor, View view) {
        ImageCardViewHolder icHolder = new ImageCardViewHolder(view);

        return icHolder;
    }

    public GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }

    public void setGridLayoutManager(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    private class ImageCardViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView mainImage;
        public TextView timeTextView;
        public CustomTextView descTextView;

        public ImageCardViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView.findViewById(R.id.title_image);
            this.mainImage = (ImageView) itemView.findViewById(R.id.main_image);
            this.timeTextView = (TextView) itemView.findViewById(R.id.time_image);
            this.descTextView = (CustomTextView) itemView.findViewById(R.id.desc_text);
        }
    }
}
