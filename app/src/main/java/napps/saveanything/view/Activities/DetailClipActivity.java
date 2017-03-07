package napps.saveanything.view.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import napps.saveanything.Database.RealmContentProvider;
import napps.saveanything.Model.Clip;
import napps.saveanything.R;
import napps.saveanything.view.customviews.CustomTextView;

public class DetailClipActivity extends AppCompatActivity {

    public static final String EXTRA_CLIP_ID = "ClipId";
    private Clip currentClip;
    private CustomTextView detailText;
    private String DATE_FORMAT = "E MMM dd, yyyy kk:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_clip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        detailText = (CustomTextView) findViewById(R.id.clip_content);
        long clipId = getIntent().getLongExtra(EXTRA_CLIP_ID, -1);
        if(clipId < 0){
            //TODO: Show a snackbar and close the activity
        }
        currentClip = RealmContentProvider.queryClip(clipId);
        detailText.setText(currentClip.getContent());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                    finish();
                break;
        }

        return true;
    }

}
