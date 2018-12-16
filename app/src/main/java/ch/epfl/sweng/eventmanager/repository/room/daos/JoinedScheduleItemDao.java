package ch.epfl.sweng.eventmanager.repository.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;

import java.util.List;
import java.util.UUID;

@Dao
public interface JoinedScheduleItemDao extends GenericEventDAO<JoinedScheduleItem, String> {
    @Query("SELECT * FROM joined_schedule_items")
    LiveData<List<JoinedScheduleItem>> getAll();

    @Query("SELECT schedule_item_id FROM joined_schedule_items")
    LiveData<List<String>> getAllIds();

    @Query("SELECT * FROM joined_schedule_items WHERE schedule_item_id IN (:scheduleItemId)")
    LiveData<List<JoinedScheduleItem>> loadAllByIds(String[] scheduleItemId);

    @Query("SELECT * FROM joined_schedule_items WHERE schedule_item_id LIKE :scheduleItemId LIMIT 1")
    LiveData<JoinedScheduleItem> findById(String scheduleItemId);

    @Query("SELECT * FROM joined_schedule_items WHERE schedule_item_id LIKE :scheduleItemId LIMIT 1")
    JoinedScheduleItem findByIdImmediate(String scheduleItemId);

    @Query("SELECT * FROM joined_schedule_items WHERE event_id LIKE :id")
    LiveData<List<JoinedScheduleItem>> findByEventId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JoinedScheduleItem... joinedScheduleItems);

    @Delete
    int delete(JoinedScheduleItem... joinedScheduleItems);

}
