package bhsystems.eu.relaycontroller.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import bhsystems.eu.relaycontroller.database.AppDatabase;


/**
 * Created by ivo on 09/06/2017.
 */

public class RelayControllerApplication extends Application {

    public static final String TAG = RelayControllerApplication.class.getSimpleName();

    private static RelayControllerApplication mInstance;


    private AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInstance.initDatabase();
    }

    private void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "RelayControllerButton").build();
    }

    public static synchronized RelayControllerApplication getInstance() {
        return mInstance;
    }

    public AppDatabase getDb() {
        return db;
    }
}