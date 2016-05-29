package napps.saveanything.Data;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.URLUtil;

import java.io.File;
import java.util.List;
import java.util.UUID;

import napps.saveanything.R;

/**
 * Created by nithesh on 5/21/2016.
 */
public class Utils {

    public static String getUniqueClipId(Context context){
        //This is a global id unique for each app installation
        //String guid = UUID.randomUUID().toString();
        //This is too long to use as unique id although it is reommended
        //So for now a combination of device unique id and currentmillis - app install millis is used as unique device id
        return Constants.CLIP_PREFIX + getUniqueDeviceId(context)+(System.currentTimeMillis() - getAppInstallTime(context));
    }

    public static void createPreferencesFiles(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
    }

    //TODO : store this during app install time
    public static void storeDeviceUniqueId(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        String deviceId =  Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        prefsEditor.putString(AppSharedPreferences.PREF_KEY_DEVICE_ID, getUniqueDeviceId(context));
        prefsEditor.commit();
    }

    //TODO : store this during app install time
    public static void storeAppInstallTime(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putLong(AppSharedPreferences.PREF_KEY_INSTALL_TIME, System.currentTimeMillis());
        prefsEditor.commit();
    }

    public static long getAppInstallTime(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        return sharedPrefs.getLong(AppSharedPreferences.PREF_KEY_INSTALL_TIME, 0);
    }

    public static String getUniqueDeviceId(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        return sharedPrefs.getString(AppSharedPreferences.PREF_KEY_DEVICE_ID, Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            return  true;
        } else {
            return false;
        }

    }

    public static boolean isNetworkUrl(String url){
        if(url == null){
            return false;
        }

        if(url.length() < 4){
            return false;
        }

        return URLUtil.isNetworkUrl(url);
    }

    // change this code as it's directly copied from net
    public static boolean isClipServiceRunning(Context context, Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        for(ActivityManager.RunningServiceInfo runningService : runningServices){
            if(runningService.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }

        return false;

    }

    //
    public static long getBufferMemorySize(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        return sharedPrefs.getLong(AppSharedPreferences.PREF_KEY_BUFFER_SIZE, getBufferSize(context));
    }

    //TODO create a method to save device ram size and buffer size
    public static long getDeviceRAM(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    //TODO get buffer size
    public static long getBufferSize(Context context){
        long ram = getDeviceRAM(context);
        //using 1/7th of it
        long buffer = ram/128;

        return buffer/1024;

    }

    //TODO : store this during app install time
    public static File getImageStoragePath() throws OutofStorageError{
            String diskState = Environment.getExternalStorageState();
            if(diskState.equals(Environment.MEDIA_MOUNTED)){
                File folder = new File(Environment.getExternalStorageDirectory(), Constants.STORAGE_MAIN_DIRECTORY);
                if(!folder.exists()){
                    folder.mkdir();
                }
                folder = new File(Environment.getExternalStorageDirectory(), Constants.STORAGE_IMAGES_DIRECTORY);
                if(!folder.exists()){
                    folder.mkdir();
                }
                long availablespace = (folder.getFreeSpace()/folder.getTotalSpace())*100;
                if(availablespace > 10){
                    return folder;
                } else {
                    throw new OutofStorageError();
                }
            } else {
                return null;
            }
    }

    public static class OutofStorageError extends Exception {

    }
}
