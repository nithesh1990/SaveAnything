package napps.saveanything.view.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.ScriptIntrinsic;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import napps.saveanything.Data.Constants;
import napps.saveanything.R;
import napps.saveanything.view.customviews.CustomTextView;

public class SaveImageActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mMainImage;
    private ProgressBar mProgressBar;
    private CustomTextView mStatusTextview;
    private LinearLayout mOptionsLayout;
    private RadioGroup mRadioGroup;
    private ImageButton mCancelButton;
    private ImageButton mDoneButton;
    private ImageButton mShowHideOptions;
    private int selectSampleSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.share_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_48dp);

        mMainImage = (ImageView) findViewById(R.id.share_image);

        mProgressBar = (ProgressBar) findViewById(R.id.share_progress_bar);
        mStatusTextview = (CustomTextView) findViewById(R.id.share_progress_text);
        mRadioGroup = (RadioGroup) findViewById(R.id.selection_layout);
        mOptionsLayout = (LinearLayout) findViewById(R.id.options_layout);
        mShowHideOptions = (ImageButton) findViewById(R.id.show_hide_options);
        mShowHideOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOptionsLayout.getVisibility() == View.VISIBLE){
                    closeSaveOptionsMenu();
                } else if(mOptionsLayout.getVisibility() == View.GONE) {
                    openSaveOptionsMenu();
                }
            }
        });
        mMainImage.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mStatusTextview.setVisibility(View.VISIBLE);
        mOptionsLayout.setVisibility(View.GONE);
        Intent shareIntent = getIntent();
        String action = shareIntent.getAction();
        String mimeType = shareIntent.getType();

        if(action.equals(Intent.ACTION_SEND) && mimeType != null){
            if(mimeType.contains(Constants.MIME_TYPE_IMAGE)){
                handleImage(shareIntent);
            } else {
                //post error
            }
            //handle single image share
        } else if(action.equals(Intent.ACTION_SEND_MULTIPLE) && mimeType != null){
            //handle multiple ima
            if(mimeType.contains(Constants.MIME_TYPE_IMAGE)){

            } else {
                //post error
            }
        }

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.save_image, menu);
        return true;
    }


    private void handleImage(Intent intent){
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(imageUri != null){
         new ImageProcessTask().execute(imageUri);
        }

    }

    @Override
    public void onClick(View v) {

    }

    private class ImageProcessTask extends AsyncTask<Uri, Void, SaveImageContent> {

        @Override
        protected SaveImageContent doInBackground(Uri... params) {
            Uri imageUri = params[0];
            //get orientation
            String scheme = imageUri.getScheme();
            int orientation = -1;
            long size = -1;
            SaveImageContent saveImageContent = new SaveImageContent();
            saveImageContent.setImageUri(imageUri);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            //this will tell to just decode bounds and not the whole image so image is not loaded into memory
            try {
                InputStream sourceStream = getApplicationContext().getContentResolver().openInputStream(imageUri);

                //just decode for bounds
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(sourceStream, null, bitmapOptions);
                //this would have been done but we need same is later to actually decode
                //SInce the stream can't be read twice so it's better to close it now and open it later
                sourceStream.close();
                int imageHeight = bitmapOptions.outHeight;
                int imageWidth = bitmapOptions.outWidth;
                //Ex: If the image
                int inSampleSize = 1;
                int recommendSample;

                //Reason for choosing LinkedHashMap is :
                //HashMap makes absolutely no guarantees about the iteration order. It can (and will) even change completely when new elements are added.
                //TreeMap will iterate according to the "natural ordering" of the keys according to their compareTo() method (or an externally supplied Comparator). Additionally, it implements the SortedMap interface, which contains methods that depend on this sort order.
                //LinkedHashMap will iterate in the order in which the entries were put into the map
                //Since we put the res in order and iterate in the same order this is most efficient and hashmap iteration is random and may take some more unnecessary time than linkedhashmap
                LinkedHashMap<Integer, String> resoptions = new LinkedHashMap<Integer, String>();
                //graphics industry standard is widthXheight

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int deviceHeight = displayMetrics.heightPixels;
                int deviceWidth = displayMetrics.widthPixels;
                Bitmap resizedBitmap = null;
                if (imageHeight >= deviceHeight || imageWidth >= deviceWidth) {
                    saveImageContent.setScaleStatus(Constants.IMAGE_SCALED_DOWN);
                    // This will keep downsampling using power of 2 until the resolution reaches
                    while ((imageHeight / inSampleSize) > deviceHeight
                            && (imageWidth / inSampleSize) > deviceWidth) {
                        resoptions.put(inSampleSize, String.valueOf((imageWidth / inSampleSize))+" X "+String.valueOf(imageHeight / inSampleSize));
                        inSampleSize *= 2;
                    }
                    resoptions.put(inSampleSize, String.valueOf((imageWidth / inSampleSize))+" X "+String.valueOf(imageHeight / inSampleSize));
                    recommendSample = inSampleSize;

                    //allow 2 powers downsampling below device resolution becasue after that image looks too blurry
                    for(int i =0; i < 2; i++){
                        inSampleSize *= 2;
                        resoptions.put(inSampleSize, String.valueOf((imageWidth / inSampleSize))+" X "+String.valueOf(imageHeight / inSampleSize));
                    }
                    
                    bitmapOptions.inJustDecodeBounds = false;
                    bitmapOptions.inSampleSize = recommendSample;
                    // even though sourcestream won't be null the stream can't be read twice with single open
                    sourceStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                    resizedBitmap = BitmapFactory.decodeStream(sourceStream, null, bitmapOptions);
                    sourceStream.close();

                } else {
                    saveImageContent.setScaleStatus(Constants.IMAGE_SCALED_UP);
                    recommendSample = inSampleSize;
                    
                    //allow scaling up until it reaches device resolution
                   while(imageHeight < deviceHeight && imageWidth < deviceWidth){
                        resoptions.put(inSampleSize, String.valueOf((imageWidth / inSampleSize))+" X "+String.valueOf(imageHeight / inSampleSize));
                        imageHeight = imageHeight * 2;
                        imageWidth = imageWidth * 2;
                    }
//                    if (scheme.equals(Constants.IMAGE_SCHEME_CONTENT)) {
//                        sourceStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
//                    } else if (scheme.equals(Constants.IMAGE_SCHEME_FILE)) {
//                        sourceStream = new FileInputStream(imageUri.getPath());
//                        long sampledsize = resizedBitmap.getByteCount();
//                    }

                    sourceStream = getApplicationContext().getContentResolver().openInputStream(imageUri);

                    resizedBitmap = BitmapFactory.decodeStream(sourceStream);

                    sourceStream.close();

                    resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, imageWidth, imageHeight, true);

                }
                resoptions.put(recommendSample, resoptions.get(recommendSample)+" (Recommended) ");
                if(scheme.contains(Constants.SCHEME_CONTENT)){
                        Cursor cursor = null;
                    try {
                        String[] orientationColumns = {MediaStore.Images.ImageColumns.ORIENTATION};
                        cursor = getApplicationContext().getContentResolver().query(imageUri, orientationColumns, null, null, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            int orientationColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);
                            if(orientationColumn != -1){
                                orientation = cursor.getInt(orientationColumn);
                            }
                        }
                    } catch (IllegalStateException e){
                        e.printStackTrace();
                    }finally {
                        if(cursor != null){
                            cursor.close();
                        }
                    }
                }

                if(orientation !=-1 && resizedBitmap != null) {
                    Matrix matrix = new Matrix();
                    switch(orientation){
                        case 90:
                            matrix.postRotate(90);
                            resizedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);
                            break;
                        case 270:
                            matrix.postRotate(270);
                            resizedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);
                            break;
                    }

                }


                saveImageContent.setBitmap(resizedBitmap);
                saveImageContent.setResOptions(resoptions);
                saveImageContent.setRecommendSample(recommendSample);
                saveImageContent.setRotation(orientation);

            } catch(FileNotFoundException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                return saveImageContent;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(SaveImageContent saveImageContent) {
            super.onPostExecute(saveImageContent);
            if(saveImageContent.getBitmap() != null){
                mProgressBar.setVisibility(View.GONE);
                mStatusTextview.setVisibility(View.GONE);
                mMainImage.setVisibility(View.VISIBLE);
                mMainImage.setImageBitmap(saveImageContent.getBitmap());
                selectSampleSize = saveImageContent.getRecommendSample();
                populateCheckBoxes(saveImageContent.getResOptions());
                openSaveOptionsMenu();

            } else {
                mProgressBar.setVisibility(View.GONE);
                mStatusTextview.setTextColor(Color.BLACK);
                mStatusTextview.setText("Error");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private void populateCheckBoxes(LinkedHashMap<Integer, String> resOptions){


        for(Map.Entry<Integer, String> entry : resOptions.entrySet()){
            AppCompatRadioButton radioButton = new AppCompatRadioButton(this);
            radioButton.setId(entry.getKey());
            radioButton.setText(entry.getValue());
            if(entry.getKey().equals(selectSampleSize)){
                radioButton.setChecked(true);
            }
            radioButton.setOnCheckedChangeListener(radioChangeListener);
            mRadioGroup.addView(radioButton);
        }
    }

    private void openSaveOptionsMenu(){
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.support.design.R.anim.design_snackbar_in);
        //slideUpAnimation.setDuration(700);
        mOptionsLayout.setAnimation(slideUpAnimation);
        mShowHideOptions.setAnimation(slideUpAnimation);
        mOptionsLayout.setVisibility(View.VISIBLE);
        mShowHideOptions.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
    }

    private void closeSaveOptionsMenu(){
        Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.support.design.R.anim.design_snackbar_out);
        //slideDownAnimation.setDuration(700);
        mOptionsLayout.setAnimation(slideDownAnimation);
        mShowHideOptions.setAnimation(slideDownAnimation);
        mOptionsLayout.setVisibility(View.GONE);
        mShowHideOptions.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

    }
    private CompoundButton.OnCheckedChangeListener radioChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            selectSampleSize = buttonView.getId();
        }
    };


    public class SaveImageContent {
        private Bitmap bitmap;
        private LinkedHashMap<Integer, String> resOptions;
        private int recommendSample;
        private int rotation;
        private Uri imageUri;
        private int scaleStatus;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public int getRecommendSample() {
            return recommendSample;
        }

        public void setRecommendSample(int recommendSample) {
            this.recommendSample = recommendSample;
        }

        public LinkedHashMap<Integer, String> getResOptions() {
            return resOptions;
        }

        public void setResOptions(LinkedHashMap<Integer, String> resOptions) {
            this.resOptions = resOptions;
        }

        public int getRotation() {
            return rotation;
        }

        public void setRotation(int rotation) {
            this.rotation = rotation;
        }

        public Uri getImageUri() {
            return imageUri;
        }

        public void setImageUri(Uri imageUri) {
            this.imageUri = imageUri;
        }

        public int getScaleStatus() {
            return scaleStatus;
        }

        public void setScaleStatus(int scaleStatus) {
            this.scaleStatus = scaleStatus;
        }
    }
}
