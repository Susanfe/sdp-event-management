package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class holds the basic elements about an organized event.<br>
 *     This class might hold way more data in the future, depending on how the backend will be organized
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
     * A list of all the concerts taking place in the event.
     */
    private List<Concert> concertList;

    public Event(int id, String name, String description, EventOrganizer organizer, Bitmap image,
                 List<Concert> concertList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.organizer = organizer;
        this.image = image;

        this.concertList = concertList;
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

    public List<Concert> getConcertList() {
        return Collections.unmodifiableList(concertList);
    }

    /**
     * Adds a concert to the event's concert list.
     * The method will return true if the concert already exists in the list.
     * @param concert Concert to be added to the List, can be null.
     * @return true if the concert is in the list after returning.
     */
    public boolean addConcert(Concert concert) {
        if (concert != null) {
            if (!concertList.contains(concert)) {
                concertList.add(concert);
            }
            return true;
        }
        return false;
    }
}
