package ch.epfl.sweng.eventmanager.repository.database;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.*;

import java.util.List;

public interface EventDao {
    @Query("SELECT * FROM 'joined-events'")
    List<Event> getAll();

    @Query("SELECT * FROM `joined-events` WHERE `event-id` IN (:eventIds)")
    List<Event> loadAllByIds(int[] eventIds);

    @Query("SELECT * FROM `joined-events` WHERE name LIKE :eventName LIMIT 1")
    Event findByName(String eventName);

    @Insert
    void insertAll(Event... events);

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void deleteAll(Event... events);

    @Delete
    void delete(Event event);

    @RawQuery
    Event getEventViaQuery(SupportSQLiteQuery query);

}
