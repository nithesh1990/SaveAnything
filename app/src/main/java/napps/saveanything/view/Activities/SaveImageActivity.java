package napps.saveanything.view.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import napps.saveanything.Data.Constants;
import napps.saveanything.R;

public class SaveImageActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener{

    private ImageView mMainImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save_image);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_action_bar)));
        fab.setOnClickListener(this);
        mMainImage = (ImageView) findViewById(R.id.share_image);
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

    private void handleImage(Intent intent){
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(imageUri != null){
            mMainImage.setImageURI(imageUri);
            //mMainImage.set
        }
    }

    @Override
    public void onClick(View v) {

    }
}
