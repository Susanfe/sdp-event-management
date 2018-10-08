package ch.epfl.sweng.eventmanager.room.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import ch.epfl.sweng.eventmanager.repository.data.Event;

@Entity(tableName = "joined_events")
public class JoinedEvent {

    public JoinedEvent(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public JoinedEvent(Event event) {
        this.uid = event.getId();
        this.name = event.getName();
    }

    @PrimaryKey
    @ColumnInfo(name = "event_id")
    private int uid;

    @ColumnInfo(name = "name")
    private String name;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

