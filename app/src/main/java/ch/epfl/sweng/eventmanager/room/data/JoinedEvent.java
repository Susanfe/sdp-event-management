package ch.epfl.sweng.eventmanager.room.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import ch.epfl.sweng.eventmanager.repository.data.Event;

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
    public JoinedEvent(Event event) {
        Objects.requireNonNull(event);
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
}

