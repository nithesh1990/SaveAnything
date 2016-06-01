package napps.saveanything.Control;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import napps.saveanything.Data.Constants;
import napps.saveanything.Data.Utils;
import napps.saveanything.Database.DBContentProvider;
import napps.saveanything.Database.DBHelper;
import napps.saveanything.Model.ClipInfo;

/**
 * Created by nithesh on 5/21/2016.
 */
/*
  Here IntentService is used instead of service because of the following reasons
  1. This should run in background indefinitely without blocking UI thread
  2. Since this runs on a separate thread, we can use infinite loops and heavy operations
  3. Since Service runs in UI thread, an infinite loop might lead ANR
 */

public class SaveClipService extends IntentService implements ClipboardManager.OnPrimaryClipChangedListener {


    private ClipboardManager mClipBoardManager;

    public SaveClipService(String name) {
        super(name);
     }

    @Override
    public void onPrimaryClipChanged() {
        Log.d("SaveAnything", "onPrimaryClipChanged: SaveAnything ");
        try {
            if (mClipBoardManager.hasPrimaryClip()) {
                ClipDescription clipDescription = mClipBoardManager.getPrimaryClipDescription();
                if (clipDescription.hasMimeType(clipDescription.MIMETYPE_TEXT_PLAIN) || clipDescription.hasMimeType(clipDescription.MIMETYPE_TEXT_HTML)) {
                    ClipData clipData = mClipBoardManager.getPrimaryClip();
                    ActivityManager appManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> runapplist = appManager.getRunningAppProcesses();
                    String packageName = runapplist.get(0).processName;
                    if (clipData != null) {
                        ClipInfo clipInfo = prepareClipInfo(clipData, packageName);
                        //clipInfo.setTitle(clipDescription.getLabel().toString());
                        DBContentProvider.insertClip(DBHelper.getInstance(this), clipInfo);
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
    protected void onHandleIntent(Intent intent) {
        Looper.prepare();
        mClipBoardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        mClipBoardManager.addPrimaryClipChangedListener(this);
        Looper.loop();

    }

    private ClipInfo prepareClipInfo(ClipData clipData, String packageName){
        ClipInfo clipInfo = new ClipInfo();
        clipInfo.setClipId(Utils.getUniqueId(this, Constants.PREFIX_CLIP));
        ClipData.Item item = clipData.getItemAt(0);
        clipInfo.setContent(item.getText().toString());
        clipInfo.setClipStatus(Constants.STATUS_CLIP_SAVED);
        clipInfo.setSourcePackage(packageName);
        if(Utils.isNetworkUrl(clipInfo.getContent())){
            clipInfo.setContentType(Constants.CLIP_HTTP_LINK);
        } else {
            clipInfo.setContentType(Constants.CLIP_PLAIN_TEXT);
        }
        clipInfo.setTimestamp(System.currentTimeMillis());
        return clipInfo;
    }



}
