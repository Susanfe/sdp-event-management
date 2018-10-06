package ch.epfl.sweng.eventmanager.room.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "joined-events")
public class JoinedEvent {


    @PrimaryKey
    @ColumnInfo(name = "event-id")
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

