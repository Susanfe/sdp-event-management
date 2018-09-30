package ch.epfl.sweng.eventmanager.backend.data;

import android.graphics.Bitmap;

/**
 * This class holds the basic elements about an organized event
 *
 * @author Louis Vialar
 */
public final class Event {
    /**
     * An internal id, identifying this event uniquely. Might have to be completed by an UUID in the future.
     */
    private final int id;
    /**
     * The name of the event
     */
    private final String name;
    /**
     * A short description of the event
     */
    private final String description;
    /**
     * An image representing the event, may be null
     */
    private final Bitmap image;

    public Event(int id, String name, String description, Bitmap image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
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

    public Bitmap getImage() {
        return image;
    }
}
