package napps.saveanything.view.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import napps.saveanything.Control.BitmapManager;
import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.R;
import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.view.customviews.CustomTextView;

/**
 * Created by nithesh on 6/26/2016.
 */
public class ImageCursorAdapter extends CursorAdapter{

    private int deviceHeight;
    private int deviceWidth;
    private BitmapManager bitmapManager;

    public ImageCursorAdapter(Context context, Cursor c, int deviceHeight, int deviceWidth) {
        super(context, c);

        bitmapManager = BitmapManager.getInstance();
        bitmapManager.initializeBM(context);
        //bitmapManager.setBitmapsViewHolder(currentView);

    }

    public void setImagesListViewforBM(ListView listview){
        bitmapManager.setImagesListview(listview);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_card_list_view, parent, false);
        IVHolder ivHolder = new IVHolder(view);
        view.setTag(ivHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        IVHolder ivHolder = (IVHolder) view.getTag();
        int position = cursor.getPosition();
        String message = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_DESC));
        String displayMsg = String.valueOf(position)+". "+message;
        ivHolder.descTextView.setText(displayMsg);
        AppLogger.addLogMessage(AppLogger.DEBUG, ImageListAdapter.class.getSimpleName(), "bindView()", "ImageListAdapter bind view called for position "+cursor.getPosition());
        try{
            //Sometimes error occurs while saving the image and image path is not saved properly
            //TODO: Have to handle that case
            int actualWidth = ivHolder.mainImage.getWidth();

            String imageUri = cursor.getString(cursor.getColumnIndex(DatabaseContract.ImageBoard.COLUMN_NAME_SAVEDPATH));
            //ivHolder.mainImage.setImageURI(Uri.parse(imageUri));
            //Glide.with(mContext).load(imageUri).placeholder(R.drawable.image_loading).fitCenter().override(actualWidth, deviceWidth).crossFade().into(ivHolder.mainImage);
            bitmapManager.setBitmap(imageUri, position, ivHolder.mainImage, deviceWidth, deviceHeight);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public class IVHolder  {

        public ImageView mainImage;
        public TextView timeTextView;
        public CustomTextView descTextView;

        public IVHolder(View itemView) {
            this.mainImage = (ImageView) itemView.findViewById(R.id.ic_main_image);
            this.timeTextView = (TextView) itemView.findViewById(R.id.ic_time_text);
            this.descTextView = (CustomTextView) itemView.findViewById(R.id.ic_desc_text);
        }
    }

}
