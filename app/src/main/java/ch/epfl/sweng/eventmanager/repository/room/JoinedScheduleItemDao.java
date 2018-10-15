package ch.epfl.sweng.eventmanager.repository.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;

import java.util.List;
import java.util.UUID;

@Dao
public interface JoinedScheduleItemDao extends GenericDAO<JoinedScheduleItem> {
    @Query("SELECT * FROM joined_schedule_items")
    LiveData<List<JoinedScheduleItem>> getAll();

    @Query("SELECT schedule_item_id FROM joined_schedule_items")
    LiveData<List<UUID>> getAllIds();

    @Query("SELECT * FROM joined_schedule_items WHERE schedule_item_id IN (:scheduleItemId)")
    LiveData<List<JoinedScheduleItem>> loadAllByIds(UUID[] scheduleItemId);

    @Query("SELECT * FROM joined_schedule_items WHERE schedule_item_id LIKE :scheduleItemId LIMIT 1")
    LiveData<JoinedScheduleItem> findById(UUID scheduleItemId);

    @Query("SELECT * FROM joined_schedule_items WHERE event_id LIKE :id LIMIT 1")
    LiveData<List<JoinedScheduleItem>> findByEventId(int id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JoinedScheduleItem... joinedScheduleItems);

    @Delete
    int delete(JoinedScheduleItem... joinedScheduleItems);

}
