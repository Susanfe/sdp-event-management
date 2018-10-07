package ch.epfl.sweng.eventmanager.room.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface JoinedEventDao {
    @Query("SELECT * FROM `joined-events`")
    LiveData<List<JoinedEvent>> getAll();

    @Query("SELECT * FROM `joined-events` WHERE `event-id` IN (:eventIds)")
    LiveData<List<JoinedEvent>> loadAllByIds(int[] eventIds);

    @Query("SELECT * FROM `joined-events` WHERE name LIKE :eventName LIMIT 1")
    LiveData<JoinedEvent> findByName(String eventName);

    @Query("SELECT * FROM `joined-events` WHERE `event-id` LIKE :eventId LIMIT 1")
    LiveData<JoinedEvent> findById(int eventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(JoinedEvent... joinedEvents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JoinedEvent joinedEvent);

    @Delete
    int deleteAll(JoinedEvent... joinedEvents);

    @Delete
    int delete(JoinedEvent joinedEvent);
}
