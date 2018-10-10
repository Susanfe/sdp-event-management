package ch.epfl.sweng.eventmanager.repository.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Defines the JoinedEvent Entity. We only store the event-id and the name of the event to minimise storage space
 */
@Entity(tableName = "joined_events")
public class JoinedEvent {

    /**
     * Default constructor needed by Room to instantiate the entity
     * @param uid event-id
     * @param name of the event
     */
    public JoinedEvent(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    /**
     * Prioritize use of this constructor instead
     * @param event not null
     */
    public JoinedEvent(@NonNull Event event) {
        this.uid = event.getId();
        this.name = event.getName();
    }

    @PrimaryKey
    @ColumnInfo(name = "event_id")
    private int uid;

    @ColumnInfo(name = "name")
    private String name;


    /* Getters and Setters */

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

    @Override
    public String toString() {
        return "JoinedEvent{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinedEvent that = (JoinedEvent) o;
        return uid == that.uid && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = uid;
        // Inspired (including magic numbers) by Java's standard hashCode()
        // method. Please refer on the original method for details.
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}

