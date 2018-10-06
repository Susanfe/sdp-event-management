package ch.epfl.sweng.eventmanager.room.data;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.*;

import java.util.List;

public interface EventDao {
    @Query("SELECT * FROM `joined-events`")
    List<JoinedEvent> getAll();

    @Query("SELECT * FROM `joined-events` WHERE `event-id` IN (:eventIds)")
    List<JoinedEvent> loadAllByIds(int[] eventIds);

    @Query("SELECT * FROM `joined-events` WHERE name LIKE :eventName LIMIT 1")
    JoinedEvent findByName(String eventName);

    @Insert
    void insertAll(JoinedEvent... joinedEvents);

    @Insert
    void insert(JoinedEvent joinedEvent);

    @Update
    void update(JoinedEvent joinedEvent);

    @Delete
    void deleteAll(JoinedEvent... joinedEvents);

    @Delete
    void delete(JoinedEvent joinedEvent);

    @RawQuery
    JoinedEvent getEventViaQuery(SupportSQLiteQuery query);

}
