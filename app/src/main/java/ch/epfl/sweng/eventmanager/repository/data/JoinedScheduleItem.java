package ch.epfl.sweng.eventmanager.repository.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.util.UUID;


@Entity(tableName = "joined_schedule_items")
public class JoinedScheduleItem {


    public JoinedScheduleItem(@NonNull UUID uid, int eventId) {
        this.uid = uid;
        this.eventId = eventId;
    }

    @PrimaryKey
    @ColumnInfo(name = "schedule_item_id")
    @NonNull
    private UUID uid;

    @ColumnInfo(name = "event_id")
    private int eventId;


    /* Getters and Setters */
    @NonNull
    public UUID getUid() {
        return uid;
    }

    void setUid(@NonNull UUID uid) {
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

        JoinedScheduleItem that = (JoinedScheduleItem) o;

        if (eventId != that.eventId) return false;
        return uid.equals(that.uid);
    }
}


