package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;

/**
 * This class represents an entity organizing an event, like an association or a faculty. It holds some information about
 * the organizer, that can be displayed on the event "about" page.
 *
 * @author Louis Vialar
 */
public final class EventOrganizer {
    /**
     * An internal id, identifying this organizer uniquely. Might have to be completed by an UUID in the future.
     */
    private int id;
    /**
     * The name of the organizer
     */
    private String name;
    /**
     * A short description of the organizer
     */
    private String description;
    /**
     * The logo of this organizer, might be null
     */
    private Bitmap logo;

    public EventOrganizer(int id, String name, String description, Bitmap logo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo = logo;
    }

    public EventOrganizer() {
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

    public Bitmap getLogo() {
        return logo;
    }
}
