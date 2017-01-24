package napps.saveanything.Control;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by "nithesh" on 11/13/2016.
 */
/*
    This is the base application class which is called first when the application starts.
    Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
    Implementations should be as quick as possible (for example using lazy initialization of state) since the time spent in this function directly impacts the performance of starting the first activity, service, or receiver in a process.
    If you override this method, be sure to call super.onCreate().
 */

public class BaseApplication extends Application {

    public static String REALM_DATABASE_NAME = "Realm_APP";
    public long REALM_SCHEMA_VERSION = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(REALM_DATABASE_NAME)
                .schemaVersion(REALM_SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /*
        This is called when the application is running low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
