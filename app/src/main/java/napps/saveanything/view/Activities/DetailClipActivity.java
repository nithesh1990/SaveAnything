package napps.saveanything.view.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import napps.saveanything.Database.RealmContentProvider;
import napps.saveanything.Model.Clip;
import napps.saveanything.R;
import napps.saveanything.view.customviews.CustomTextView;

public class DetailClipActivity extends AppCompatActivity {

    public static final String EXTRA_CLIP_ID = "ClipId";
    private Clip currentClip;
    private CustomTextView detailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_clip);
        detailText = (CustomTextView) findViewById(R.id.clip_content);
        long clipId = getIntent().getLongExtra(EXTRA_CLIP_ID, -1);
        if(clipId < 0){
            //TODO: Show a snackbar and close the activity
        }
        currentClip = RealmContentProvider.queryClip(clipId);
        detailText.setText(currentClip.getContent());
    }
}
