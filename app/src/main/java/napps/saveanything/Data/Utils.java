package napps.saveanything.Data;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.webkit.URLUtil;

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
        return Constants.CLIP_TYPE_PREFIX + getUniqueDeviceId(context)+(System.currentTimeMillis() - getAppInstallTime(context));
    }

    public static void createPreferencesFiles(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
    }

    public static void storeDeviceUniqueId(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        String deviceId =  Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        prefsEditor.putString(Constants.PREF_KEY_DEVICE_ID, getUniqueDeviceId(context));
        prefsEditor.commit();
    }

    public static void storeAppInstallTime(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putLong(Constants.PREF_KEY_INSTALL_TIME, System.currentTimeMillis());
        prefsEditor.commit();
    }

    public static long getAppInstallTime(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        return sharedPrefs.getLong(Constants.PREF_KEY_INSTALL_TIME, 0);
    }

    public static String getUniqueDeviceId(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        return sharedPrefs.getString(Constants.PREF_KEY_DEVICE_ID, Settings.Secure.getString(context.getContentResolver(),
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
}
