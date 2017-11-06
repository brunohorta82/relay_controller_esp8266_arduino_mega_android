package bhsystems.eu.relaycontroller.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import bhsystems.eu.relaycontroller.model.RelayControllerButton;

/**
 * Created by ivo on 20/06/2017.
 */

@Dao
public interface RelayControllerButtonDao {
    @Query("SELECT COUNT(*) FROM relaycontrollerbutton")
    int count();

    @Query("SELECT * FROM relaycontrollerbutton")
    List<RelayControllerButton> getAll();

    @Query("SELECT * FROM relaycontrollerbutton WHERE pin IN (:pins)")
    List<RelayControllerButton> loadAllByIds(String[] pins);

    @Query("SELECT * FROM relaycontrollerbutton WHERE pin = :pin LIMIT 1")
    RelayControllerButton findByPin(int pin);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RelayControllerButton... relayControllerButtons);

    @Delete
    void delete(RelayControllerButton podManRecord);

    @Update
    void updateRelayControllerButton(RelayControllerButton relayControllerButton);

}