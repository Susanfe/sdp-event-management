package ch.epfl.sweng.eventmanager.repository.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.UUID;


@Entity(tableName = "joined_schedule_activities")
public class JoinedScheduleActivity {


    public JoinedScheduleActivity(UUID uid, int eventId) {
        this.uid = uid;
        this.eventId = eventId;
    }

    @PrimaryKey
    @ColumnInfo(name = "schedule_activity_id")
    private UUID uid;

    @ColumnInfo(name = "event_id")
    private int eventId;


    /* Getters and Setters */

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinedScheduleActivity that = (JoinedScheduleActivity) o;

        if (eventId != that.eventId) return false;
        return uid != null ? uid.equals(that.uid) : that.uid == null;
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + eventId;
        return result;
    }

    @Override
    public String toString() {
        return "JoinedScheduleActivity{" +
                "uid=" + uid +
                ", eventId=" + eventId +
                '}';
    }
}


