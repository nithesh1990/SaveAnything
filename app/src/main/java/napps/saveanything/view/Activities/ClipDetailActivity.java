package napps.saveanything.view.Activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import napps.saveanything.Model.Clip;
import napps.saveanything.R;
import napps.saveanything.view.customviews.CustomTextView;

public class ClipDetailActivity extends AppCompatActivity {


    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppbarLayout;
    private Clip mCurrentClip;
    private CustomTextView mDetailText;

    private String DATE_FORMAT = "E MMM dd, yyyy kk:mm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.clip_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mCurrentClip = (Clip)getIntent().getSerializableExtra("ClipObject");
        initViews();
        setUpViews();
    }

    protected void initViews(){
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppbarLayout = (AppBarLayout) findViewById(R.id.clip_app_bar);
        mDetailText = (CustomTextView) findViewById(R.id.clip_detail_text);
    }
    protected void setUpViews(){
        mCollapsingToolbarLayout.setTitle(getDateandTime(mCurrentClip.getTimeStamp()));
        mDetailText.setText(mCurrentClip.getContent());
    }

    public String getDateandTime(long timestamp){
        String time = new SimpleDateFormat(DATE_FORMAT).format(new Date(timestamp));
        return time;
    }
}
