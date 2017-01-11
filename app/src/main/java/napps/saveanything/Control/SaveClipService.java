package napps.saveanything.Control;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.util.Log;

import java.util.List;

import io.realm.RealmConfiguration;
import napps.saveanything.Database.RealmContentProvider;
import napps.saveanything.Model.Builder;
import napps.saveanything.Model.Clip;
import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.Utilities.Utils;
import napps.saveanything.Database.DBContentProvider;
import napps.saveanything.Database.DBHelper;
import napps.saveanything.Model.ClipInfo;

/**
 * Created by nithesh on 5/21/2016.
 */
/*
  Here IntentService is not used because of the following reasons
  1. Intent Service runs on a separate thread which creates overhead for thread creation
  2. We could have used infinite loop inside Intent Service which keeps service running indefinitely but that's not a good design as it consumes more memory and battery and intent service is intended for asynchronous short term task and not for long term task
  3. By just returning
 */

public class SaveClipService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {


    //private static design
    //Static: Static variables are stored in method area which is mentioned in official documentation
    //      The Method Area
    //          Inside a Java Virtual Machine instance, information about loaded types is stored in a logical area of memory called the method area.
    //          When the Java Virtual Machine loads a type, it uses a class loader to locate the appropriate class file. The class loader reads in the class file--a linear stream of binary data-- and passes it to the virtual machine.
    //          The virtual machine extracts information about the type from the binary data and stores the information in the method area. Memory for class (static) variables declared in the class is also taken from the method area.
    //          So the static variables aren't removed until
    //When will static variables be garbage collected
    //      Static variables are referenced by Class objects which are referenced by ClassLoaders.
    //      So unless either the ClassLoader drops the Class somehow (if that's even possible) or the ClassLoader itself becomes eligible for collection (more likely - think of unloading webapps) the static variables (or rather, the objects they reference) won't be collected.
    //      Java program can be dynamically extended at run time by loading new types through user-defined class loaders.
    //      All these loaded types occupy space in the method area.
    //      So just like normal heaps the memory footprints of method areas grows and hence types can be freed as well by unloading if they are no longer needed. If the application has no references to a given type, then the type can be unloaded or garbage collected (like heap memory).
    //For more details about Class loading and unloading see my google drive doc about java
    //So the probability of this getting GC is less
    //mClipBoardManager just holds the reference of system clipboard manager
    //but we have to hold the reference of listener. How doess this work ?
    //Yet to decide.

    private static ClipboardManager mClipBoardManager;

    //getName() and getCanonicalName() would return saveAnything.Control.SaveClipService which is not necessary for us. We just need a simple name for logging purpose
    private String  CLASS_TAG = this.getClass().getSimpleName();

    //Sometimes OnPrimaryClipChanged() is called twice. To avoid duplicate insertion of data we check if new clip is different form last clip
    //Even though user purposefully copied it 2 times only one clip makes sense.
    //This colud have been done by querying last clip inserted but that's too costly for an issue that occurs occassionally
    private String last_Clip_Inserted = "";

    public SaveClipService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnStartCommand", "Called");
        if(mClipBoardManager == null){
            mClipBoardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            mClipBoardManager.addPrimaryClipChangedListener(this);
            AppLogger.addLogMessage(AppLogger.INFO, CLASS_TAG, "OnStartCommand", "Registered ClipBoard Listener");
        }
        //We need the service to be restarted so we are using START_STICKY. We are not using intent so we don't need to use START_REDELIVER_INTENT
        return START_STICKY;
    }

    @Override
    public void onTrimMemory(int level) {
        switch (level){
            //Both these cases are safe to run service, Release resources like
            //the process has gone on to the LRU list.
            case TRIM_MEMORY_BACKGROUND:
                AppLogger.addLogMessage(AppLogger.INFO, CLASS_TAG, "OnTrimMemory", "Process is moved to LRU list");
                break;
            //the process had been showing a user interface, and is no longer doing so.
            case TRIM_MEMORY_UI_HIDDEN:
            // do nothing as of now
                AppLogger.addLogMessage(AppLogger.INFO, CLASS_TAG, "OnTrimMemory", "Process UI is Hidden");
                break;

            //These cases are moderate and though the process will not be killed immediately
            //the process is around the middle of the background LRU list; freeing memory can help the system keep other processes running later in the list for better overall performance.
            case TRIM_MEMORY_MODERATE:
            //the process is not an expendable background process, but the device is running moderately low on memory.
            case TRIM_MEMORY_RUNNING_MODERATE:
            //the process is not an expendable background process, but the device is running low on memory.
            case TRIM_MEMORY_RUNNING_LOW:
                AppLogger.addLogMessage(AppLogger.INFO, CLASS_TAG, "OnTrimMemory", "Device is moderately running low on memory");


                //These cases are critical and the process can be killed at any point of time
            //the process is not an expendable background process, but the device is running extremely low on memory and is about to not be able to keep any background processes running.
            //OnlowMemory() is called after this
            case TRIM_MEMORY_RUNNING_CRITICAL:
            //the process is nearing the end of the background LRU list, and if more memory isn't found soon it will be killed.
            case TRIM_MEMORY_COMPLETE:
                AppLogger.addLogMessage(AppLogger.INFO, CLASS_TAG, "OnTrimMemory", "Device memory in critical");
                scheduleServiceAlarm(2 * Constants.MINUTE);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //super.onTaskRemoved(rootIntent);
        //We need to send a pending intent using alarm manager
        //which is received by a class extending broadcast receiver
        //this class receives different alarms (immediate alarms, every 1 hour alarms, 12 hour alarms)
        //and makes sure everything
        scheduleServiceAlarm(30 * Constants.SECOND);
    }

    private void scheduleServiceAlarm(long duration){
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnTaskRemoved", "Called");
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Constants.BROADCAST_START_SERVICE, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+Constants.MINUTE, pendingIntent);
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnTaskRemoved", "Scheduled alarm for "+duration+" milliseconds");

    }
    @Override
    public void onPrimaryClipChanged() {
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "onPrimaryClipChanged", "Called");
            try {
            if (mClipBoardManager.hasPrimaryClip()) {
                ClipDescription clipDescription = mClipBoardManager.getPrimaryClipDescription();
                if (clipDescription.hasMimeType(clipDescription.MIMETYPE_TEXT_PLAIN) || clipDescription.hasMimeType(clipDescription.MIMETYPE_TEXT_HTML)) {
                    ClipData clipData = mClipBoardManager.getPrimaryClip();
                    //Add a check for android version here as in latest releases this is being deprecated
                    ActivityManager appManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> runapplist = appManager.getRunningAppProcesses();
                    String packageName = runapplist.get(0).processName;
                    if (clipData != null) {
                        String clipContent = clipData.getItemAt(0).getText().toString();
                        //check if the clip is not same as last clip
                        if(!last_Clip_Inserted.equals(clipContent)) {
                            Builder.ClipBuilder clipBuilder = buildClip(packageName, clipContent);
                            //clipInfo.setTitle(clipDescription.getLabel().toString());
                            RealmContentProvider.insertClip(clipBuilder);
                            //if (DBContentProvider.insertClip(DBHelper.getInstance(this).getWritableDatabase(), clipInfo)) {
                                AppLogger.addLogMessage(AppLogger.INFO, CLASS_TAG, "OnPrimaryClipChanged", "Inserted clip with Id " + clipBuilder.clipId);
                                last_Clip_Inserted = clipContent;
                            //}
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

/*Old Implementation
    private ClipInfo buildClipInfo(ClipData clipData, String packageName, String content){
        ClipInfo.ClipBuilder clipBuilder = new ClipInfo.ClipBuilder();
        clipBuilder.ClipId(Utils.getUniqueId(this, Constants.PREFIX_CLIP));
        clipBuilder.Content(content);
        clipBuilder.ClipStatus(Constants.STATUS_CLIP_SAVED);
        clipBuilder.SourcePackage(packageName);
        if(Utils.isNetworkUrl(content)){
            clipBuilder.ContentType(Constants.CLIP_HTTP_LINK);
        } else {
            clipBuilder.ContentType(Constants.CLIP_PLAIN_TEXT);
        }
        clipBuilder.Timestamp(System.currentTimeMillis());
        return clipBuilder.build();
    }

*/

    private Builder.ClipBuilder buildClip(String packageName, String content){
        long idOrTimeStamp = System.currentTimeMillis();

        /* We can't stop builder in between to check and add content Type So we are using ternary operator
        if(Utils.isNetworkUrl(content)){
            contentType = Constants.CLIP_HTTP_LINK;
        } else {
            contentType = Constants.CLIP_PLAIN_TEXT;
        }*/
        Builder.ClipBuilder clipBuilder = new Builder.ClipBuilder(idOrTimeStamp);
        return clipBuilder.ClipId(Utils.getUniqueId(this, Constants.PREFIX_CLIP, idOrTimeStamp))
                    .Content(content)
                    .ClipStatus(Constants.STATUS_CLIP_SAVED)
                    .SourcePackage(packageName)
                    .ContentType(Utils.isNetworkUrl(content) ? Constants.CLIP_HTTP_LINK : Constants.CLIP_PLAIN_TEXT)
                    .Timestamp(idOrTimeStamp);

    }
}
