package ch.epfl.sweng.eventmanager.repository.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;

import java.util.List;

/**
 * Defines the Data Access Object of the JoinedEvent Entity.
 * Here are all the SQL queries.
 */
@Dao
public interface JoinedEventDao extends GenericDAO<JoinedEvent> {
    @Query("SELECT * FROM joined_events")
    LiveData<List<JoinedEvent>> getAll();

    @Query("SELECT event_id FROM joined_events")
    LiveData<List<Integer>> getAllIds();

    @Query("SELECT * FROM joined_events WHERE event_id IN (:eventIds)")
    LiveData<List<JoinedEvent>> loadAllByIds(int[] eventIds);

    @Query("SELECT * FROM joined_events WHERE name LIKE :eventName LIMIT 1")
    LiveData<JoinedEvent> findByName(String eventName);

    @Query("SELECT * FROM joined_events WHERE event_id LIKE :eventId LIMIT 1")
    LiveData<JoinedEvent> findById(int eventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JoinedEvent... joinedEvent);

    @Delete
    int delete(JoinedEvent... joinedEvent);
}
