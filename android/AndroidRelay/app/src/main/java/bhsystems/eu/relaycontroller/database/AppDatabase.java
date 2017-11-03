package bhsystems.eu.relaycontroller.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import bhsystems.eu.relaycontroller.entity.RelayControllerButton;

/**
 * Created by ivo on 20/06/2017.
 */

@Database(entities = {RelayControllerButton.class}, version = 3)
@TypeConverters(value = DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RelayControllerButtonDao relayControllerButtonDao();
}