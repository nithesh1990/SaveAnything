package napps.saveanything.view.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.rebound.BaseSpringSystem;

import io.realm.Realm;
import io.realm.RealmResults;
import napps.saveanything.Control.BitmapManager;
import napps.saveanything.Model.Image;
import napps.saveanything.R;
import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.view.Fragments.ImageFragment;
import napps.saveanything.view.customviews.CustomImageView;
import napps.saveanything.view.customviews.CustomTextView;

/**
 * Created by nithesh on 5/13/2016.
 */
public class ImageListAdapter extends RecyclerRealmAdapter<ImageListAdapter.ImageCardViewHolder, Image> implements View.OnClickListener{

    private int deviceHeight;
    private int deviceWidth;

    private int viewMode;
    private BitmapManager bitmapManager;
    private Context mContext;


    public ImageListAdapter(Context context, RecyclerView currentView, int layout, RealmResults<Image> imagesList, int deviceWidth, int deviceHeight, int viewMode, BaseSpringSystem baseSpringSystem) {
        super(context, layout, imagesList, baseSpringSystem);
        this.deviceHeight = deviceHeight;
        this.deviceWidth = deviceWidth;
        this.mContext = context;
        this.viewMode = viewMode;
        bitmapManager = BitmapManager.getInstance();
        bitmapManager.initializeBM(context);
        bitmapManager.setBitmapsViewHolder(currentView);
    }

    @Override
    public void bindView(ImageCardViewHolder holder, Image image, int position) {
        ImageCardViewHolder imageCardHolder = (ImageCardViewHolder) holder;
        imageCardHolder.attachedView.setTag(image);
        imageCardHolder.favoriteButton.setTag(image);
        imageCardHolder.shareButton.setTag(image);
        imageCardHolder.moreButton.setTag(image);
        //String message = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_DESC));
        String message = image.getDesc();
        if(message != null && !message.isEmpty()) {
            imageCardHolder.descTextView.setText(message);
        } else {
            imageCardHolder.descTextView.setText("");
        }
        //String timeText = getTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_TIMESTAMP)));
        String timeText = getTimeText(image.getTimeStamp());
        imageCardHolder.timeTextView.setText(timeText);
        setFavoriteIcon(imageCardHolder.favoriteButton, image.isFavorite());
        AppLogger.addLogMessage(AppLogger.DEBUG, ImageListAdapter.class.getSimpleName(), "bindView()", "ImageListAdapter bind view called for position "+position);
        try{
            //Sometimes error occurs while saving the image and image path is not saved properly
            //TODO: Have to handle that case
            int actualWidth = imageCardHolder.mainImage.getWidth();

            //String imageUri = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH));
            String imageUri = image.getOriginalPath();
//            if(image.getSavedPath() != null && !image.getSavedPath().isEmpty()){
//                imageUri = image.getSavedPath();
//            } else {
//                imageUri = image.getOriginalPath();
//            }
            //imageCardHolder.mainImage.setImageURI(Uri.parse(imageUri));
            int boxSize = imageCardHolder.mainImage.getWidth();
            imageCardHolder.mainImage.setImageDrawable(null);

            if(viewMode == ImageFragment.GRID_MODE){
                Glide.with(mContext).load(imageUri).override(deviceWidth/2, deviceWidth/2).centerCrop().into(imageCardHolder.mainImage);
            } else {
                Glide.with(mContext).load(imageUri).override(deviceWidth, (9*deviceWidth)/16).centerCrop().into(imageCardHolder.mainImage);
            }
            //imageCardHolder.mainImage.setImageURI(Uri.parse(imageUri));
            //Glide.with(mContext).load(imageUri).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(R.drawable.image_loading).into(imageCardHolder.mainImage);
            //Glide.with(mContext).load(imageUri).asBitmap().transform(new GlideBitmapTransformation(mContext, (deviceWidth/2), position, viewMode)).placeholder(R.drawable.image_loading).into(imageCardHolder.mainImage);
            //bitmapManager.setBitmap(imageUri, position, imageCardHolder.mainImage, deviceWidth, deviceHeight);

        }catch(Exception e){
            e.printStackTrace();
        }
         //imageCardHolder.mainImage.is
    }

    @Override
    public ImageCardViewHolder newView(ViewGroup parent, Context context, View view) {
        ImageCardViewHolder icHolder = new ImageCardViewHolder(view);

        return icHolder;
    }

    @Override
    public boolean changeFavorite(CustomImageView favoriteIcon) {
        Image image = (Image)favoriteIcon.getTag();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        image.setFavorite(!image.isFavorite());
        realm.copyToRealmOrUpdate(image);
        realm.commitTransaction();
        return image.isFavorite();
    }

    public void setGridLayoutManager(GridLayoutManager gridLayoutManager) {

        this.bitmapManager.setmLayoutManager(gridLayoutManager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Image image = (Image)view.getTag();
        switch (id) {
            case R.id.image_card :
                //Image Detail Activity
                //Intent detailIntent = new Intent(mContext, ClipDetailActivity.class);
                //detailIntent.putExtra("ClipId", clip.getId());
                //mContext.startActivity(detailIntent);
                break;
            case R.id.ic_share_button:
                //Implement share feature
                break;
        }

    }



    public class ImageCardViewHolder extends RecyclerView.ViewHolder {

        View attachedView;
        CustomImageView mainImage;
        TextView timeTextView;
        CustomTextView descTextView;
        CustomImageView favoriteButton;
        CustomImageView shareButton;
        CustomImageView moreButton;

        public ImageCardViewHolder(View itemView) {
            super(itemView);
            attachedView = itemView;
            attachedView.setOnClickListener(ImageListAdapter.this);
            mainImage = (CustomImageView) itemView.findViewById(R.id.ic_main_image);
            timeTextView = (TextView) itemView.findViewById(R.id.ic_time_text);
            descTextView = (CustomTextView) itemView.findViewById(R.id.ic_desc_text);
            //int iconColorFilter = getIconColorFilter(R.styleable.CustomIconColors_iconColorNormal);
            favoriteButton = (CustomImageView) itemView.findViewById(R.id.ic_favorite);
            favoriteButton.setOnTouchListener(ImageListAdapter.this);
            //this.favoriteButton.setColorFilter(iconColorFilter);
            shareButton = (CustomImageView) itemView.findViewById(R.id.ic_share_button);
            shareButton.setOnClickListener(ImageListAdapter.this);
            //this.shareButton.setColorFilter(iconColorFilter);
            moreButton = (CustomImageView) itemView.findViewById(R.id.ic_more_button);
            moreButton.setOnClickListener(ImageListAdapter.this);
            //this.moreButton.setColorFilter(iconColorFilter);

        }
    }


}
