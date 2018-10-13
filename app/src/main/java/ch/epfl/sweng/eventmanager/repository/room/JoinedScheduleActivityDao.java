package ch.epfl.sweng.eventmanager.repository.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleActivity;

import java.util.List;
import java.util.UUID;

@Dao
public interface JoinedScheduleActivityDao {
    @Query("SELECT * FROM joined_schedule_activities")
    LiveData<List<JoinedScheduleActivity>> getAll();

    @Query("SELECT schedule_activity_id FROM joined_schedule_activities")
    LiveData<List<UUID>> getAllIds();

    @Query("SELECT * FROM joined_schedule_activities WHERE schedule_activity_id IN (:scheduleActivityId)")
    LiveData<List<JoinedScheduleActivity>> loadAllByIds(UUID[] scheduleActivityId);

    @Query("SELECT * FROM joined_schedule_activities WHERE schedule_activity_id LIKE :scheduleActivityId LIMIT 1")
    LiveData<JoinedScheduleActivity> findById(UUID scheduleActivityId);

    @Query("SELECT * FROM joined_schedule_activities WHERE event_id LIKE :id LIMIT 1")
    LiveData<List<JoinedScheduleActivity>> findByEventId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JoinedScheduleActivity... joinedScheduleActivities);

    @Delete
    int delete(JoinedScheduleActivity... joinedScheduleActivities);
}
