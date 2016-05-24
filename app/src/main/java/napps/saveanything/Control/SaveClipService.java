package napps.saveanything.Control;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import napps.saveanything.Data.Constants;
import napps.saveanything.Data.Utils;
import napps.saveanything.Database.DBContentProvider;
import napps.saveanything.Database.DBHelper;
import napps.saveanything.Model.ClipInfo;

/**
 * Created by nithesh on 5/21/2016.
 */
public class SaveClipService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {

    private ClipboardManager mClipBoardManager;
    private Context mContext;

    @Override
    public void onPrimaryClipChanged() {
        Log.d("SaveAnything", "onPrimaryClipChanged: SaveAnything ");
        try {
            if (mClipBoardManager.hasPrimaryClip()) {
                ClipDescription clipDescription = mClipBoardManager.getPrimaryClipDescription();
                if (clipDescription.hasMimeType(clipDescription.MIMETYPE_TEXT_PLAIN) || clipDescription.hasMimeType(clipDescription.MIMETYPE_TEXT_HTML)) {
                    ClipData clipData = mClipBoardManager.getPrimaryClip();
                    if (clipData != null) {
                        ClipInfo clipInfo = prepareClipInfo(clipData);
                        //clipInfo.setTitle(clipDescription.getLabel().toString());
                        DBContentProvider.insertClip(DBHelper.getInstance(mContext), clipInfo);
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mClipBoardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mClipBoardManager.addPrimaryClipChangedListener(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }


    private ClipInfo prepareClipInfo(ClipData clipData){
        ClipInfo clipInfo = new ClipInfo();
        clipInfo.setClipId(Utils.getUniqueClipId(mContext));
        ClipData.Item item = clipData.getItemAt(0);
        clipInfo.setContent(item.getText().toString());
        clipInfo.setClipStatus(Constants.CLIP_SAVED);
        if(Utils.isNetworkUrl(clipInfo.getContent())){
            clipInfo.setContentType(Constants.CLIP_HTTP_LINK);
        } else {
            clipInfo.setContentType(Constants.CLIP_PLAIN_TEXT);
        }
        clipInfo.setTimestamp(System.currentTimeMillis());
        return clipInfo;
    }



}
