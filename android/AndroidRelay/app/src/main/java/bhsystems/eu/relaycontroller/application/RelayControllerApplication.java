package bhsystems.eu.relaycontroller.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import bhsystems.eu.relaycontroller.database.AppDatabase;


/**
 * Created by ivo on 09/06/2017.
 */

public class RelayControllerApplication extends Application {

    private static RelayControllerApplication mInstance;

    private AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInstance.db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "RelayControllerButton").build();
    }


    public static synchronized RelayControllerApplication sharedInstance() {
        return mInstance;
    }

    public AppDatabase getDb() {
        return db;
    }
}