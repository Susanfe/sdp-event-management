package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the basic elements about an organized event.<br>
 * This class might hold way more data in the future, depending on how the backend will be organized
 *
 * @author Louis Vialar
 */
public final class Event {
    /**
     * An internal id, identifying this event uniquely. Might have to be completed by an UUID in the future.
     */
    private int id;
    /**
     * The name of the event
     */
    private String name;
    /**
     * A short description of the event
     */
    private String description;
    /**
     * The entity organizing this event
     */
    private EventOrganizer organizer;
    /**
     * An image representing the event, may be null
     */
    private Bitmap image;
    /**
     * The location of the event
     */
    private EventLocation location;
    /**
     * A particular place into the event
     */
    private List<Spot> spotList;

    public Event(int id, String name, String description, EventOrganizer organizer, Bitmap image, EventLocation location, List<Spot> spotList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.organizer = organizer;
        this.image = image;
        this.location = location;
        this.spotList = new ArrayList<>(spotList);
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public EventOrganizer getOrganizer() {
        return organizer;
    }

    public Bitmap getImage() {
        return image;
    }

    public EventLocation getLocation() {
        return location;
    }

    public List<Spot> getSpotList() { return spotList; }
}
