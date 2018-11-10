package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
     * Indicates the start time of the even, precision required is minutes.
     * This is a long because firebase doesn't understand Date objects.
     */
    private long beginDate;
    /**
     * Indicates the end time of the even, precision required is minutes.
     * This is a long because firebase doesn't understand Date objects.
     */
    private long endDate;
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

    /**
     * The twitter account screen name
     */
    private String twitterName;

    // TODO define if an event can have only empty and null atributes

    public Event(int id, String name, String description, Date beginDate, Date endDate, EventOrganizer organizer, Bitmap image, EventLocation location, List<Spot> spotList, String twitterName) {
        if (beginDate.getTime() > endDate.getTime())
            throw new IllegalArgumentException("The time at the start of the event should be later than the time at the end");

        this.id = id;
        this.name = name;
        this.beginDate = beginDate.getTime();
        this.endDate = endDate.getTime();
        this.description = description;
        this.organizer = organizer;
        this.image = image;
        this.location = location;
        this.spotList = new ArrayList<>(spotList);
        this.twitterName = twitterName;
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBeginDate() {
        if (beginDate <= 0) {
            return null;
        }
        return new Date(beginDate);
    }

    public Date getEndDate() {
        if (endDate <= 0) {
            return null;
        }
        return new Date(endDate);
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

    public String getTwitterName() {
        return this.twitterName;
    }

    public String beginDateAsString() {
        if (beginDate <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        return f.format(beginDate);
    }

    public String endDateAsString(){
        if (endDate <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        return f.format(endDate);
    }

    // TODO put setters ??
}
