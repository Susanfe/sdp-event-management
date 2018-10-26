package ch.epfl.sweng.eventmanager.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;


@Entity(tableName = "joined_schedule_items")
public class JoinedScheduleItem {


    public JoinedScheduleItem(UUID uid, int eventId) {
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

        JoinedScheduleItem that = (JoinedScheduleItem) o;

        if (eventId != that.eventId) return false;
        return uid != null ? uid.equals(that.uid) : that.uid == null;
    }
}


