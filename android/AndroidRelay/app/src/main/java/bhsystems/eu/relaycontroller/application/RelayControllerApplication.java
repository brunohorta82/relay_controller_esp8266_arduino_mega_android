package bhsystems.eu.relaycontroller.application;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import bhsystems.eu.relaycontroller.database.AppDatabase;
import bhsystems.eu.relaycontroller.entity.RelayControllerButton;


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
        new AsyncTask<AppDatabase, Void, Void>() {
            @Override
            protected Void doInBackground(AppDatabase... params) {
                AppDatabase db = params[0];
                if(db.relayControllerButtonDao().count() == 0){
                    db.relayControllerButtonDao().insertAll(new RelayControllerButton("Button 1", RelayControllerButton.RelayControllerButtonType.TOGGLE, 1));
                }
                return null;
            }
        }.execute(db);

    }

    public static synchronized RelayControllerApplication getInstance() {
        return mInstance;
    }

    public AppDatabase getDb() {
        return db;
    }
}