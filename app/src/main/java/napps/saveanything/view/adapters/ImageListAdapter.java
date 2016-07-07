package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ViewTarget;

import napps.saveanything.Control.BitmapManager;
import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.R;
import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.view.Fragments.ImageFragment;
import napps.saveanything.view.customviews.CustomTextView;
import napps.saveanything.view.customviews.GlideBitmapTransformation;

/**
 * Created by nithesh on 5/13/2016.
 */
public class ImageListAdapter extends RecyclerCursorAdapter {

    private int deviceHeight;
    private int deviceWidth;

    private int viewMode;
    private BitmapManager bitmapManager;
    private Context mContext;

    public ImageListAdapter(Context context, RecyclerView currentView, int layout, Cursor cursor, int deviceWidth, int deviceHeight, int viewMode) {
        super(context, layout, cursor);
        this.deviceHeight = deviceHeight;
        this.deviceWidth = deviceWidth;
        this.mContext = context;
        this.viewMode = viewMode;
        bitmapManager = BitmapManager.getInstance();
        bitmapManager.initializeBM(context);
        bitmapManager.setBitmapsViewHolder(currentView);

    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, Cursor cursor, int position) {
        ImageCardViewHolder imageCardHolder = (ImageCardViewHolder) holder;
        String message = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_DESC));
        if(message != null && !message.isEmpty()) {
            imageCardHolder.descTextView.setText(message);
        } else {
            imageCardHolder.descTextView.setText("");
        }
        String timeText = getTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP)));
        imageCardHolder.timeTextView.setText(timeText);
        AppLogger.addLogMessage(AppLogger.DEBUG, ImageListAdapter.class.getSimpleName(), "bindView()", "ImageListAdapter bind view called for position "+cursor.getPosition());
        try{
            //Sometimes error occurs while saving the image and image path is not saved properly
            //TODO: Have to handle that case
            int actualWidth = imageCardHolder.mainImage.getWidth();

            String imageUri = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH));
            //imageCardHolder.mainImage.setImageURI(Uri.parse(imageUri));
            int boxSize = imageCardHolder.mainImage.getWidth();
            imageCardHolder.mainImage.setImageDrawable(null);

            if(viewMode == ImageFragment.GRID_MODE){
                Glide.with(mContext).load(imageUri).override(deviceWidth/2, deviceWidth/2).centerCrop().into(imageCardHolder.mainImage);
            } else {
                Glide.with(mContext).load(imageUri).override(deviceWidth, (9*deviceWidth)/16).centerCrop().into(imageCardHolder.mainImage);
            }
            //Glide.with(mContext).load(imageUri).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(R.drawable.image_loading).into(imageCardHolder.mainImage);
            //Glide.with(mContext).load(imageUri).asBitmap().transform(new GlideBitmapTransformation(mContext, (deviceWidth/2), position, viewMode)).placeholder(R.drawable.image_loading).into(imageCardHolder.mainImage);
            //bitmapManager.setBitmap(imageUri, position, imageCardHolder.mainImage, deviceWidth, deviceHeight);

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

    public void setGridLayoutManager(GridLayoutManager gridLayoutManager) {

        this.bitmapManager.setmLayoutManager(gridLayoutManager);
    }


    public class ImageCardViewHolder extends RecyclerView.ViewHolder {

        public ImageView mainImage;
        public TextView timeTextView;
        public CustomTextView descTextView;

        public ImageCardViewHolder(View itemView) {
            super(itemView);
            this.mainImage = (ImageView) itemView.findViewById(R.id.ic_main_image);
            this.timeTextView = (TextView) itemView.findViewById(R.id.ic_time_text);
            this.descTextView = (CustomTextView) itemView.findViewById(R.id.ic_desc_text);
        }
    }


}
