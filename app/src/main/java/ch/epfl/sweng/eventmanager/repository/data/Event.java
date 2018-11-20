package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;
import ch.epfl.sweng.eventmanager.users.Role;

import java.text.SimpleDateFormat;
import java.util.*;

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
     * A map from roles to a list of user UIDs.
     */
    private Map<String, Map<String, String>> users;

    /**
     * The twitter account screen name
     */
    private String twitterName;

    private EventTicketingConfiguration ticketingConfiguration;

    // TODO define if an event can have only empty and null atributes
    public Event(int id, String name, String description, Date beginDate, Date endDate,
                 EventOrganizer organizer, Bitmap image, EventLocation location,
                 List<Spot> spotList, Map<String, Map<String, String>> users, String twitterName) {
        this(id, name, description, beginDate, endDate, organizer, image, location, spotList, users, twitterName, null);
    }

    public Event(int id, String name, String description, Date beginDate, Date endDate,
                 EventOrganizer organizer, Bitmap image, EventLocation location,
                 List<Spot> spotList, Map<String, Map<String, String>> users, String twitterName, EventTicketingConfiguration ticketingConfiguration) {
        this.ticketingConfiguration = ticketingConfiguration;

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
        this.users = users;
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

    /**
     * Do not use, only public due to a limitation of our auto-matching with Firebase.
     *
     * @return firebase representation of user permissions.
     */
    public Map<String, Map<String, String>> getUsers() { return users; }

    /**
     * @return a map of Role permissions to User IDs
     */
    public Map<Role, List<String>> getPermissions() {
        Map<Role, List<String>> result = new HashMap<>();

        // Don't blow up if the event does not contain any permission data
        if (getUsers() == null) return result;
        if (getUsers().keySet() == null) return result;


        // The keys of a Java Map are unique
        for (String rawRole : getUsers().keySet()) {
            Role role = Role.valueOf(rawRole.toUpperCase());
            List<String> users = new ArrayList<>();
            for (String uid : getUsers().get(rawRole).values()) {
                users.add(uid);
            }

            result.put(role, users);
        }

        return result;
    }

    public Collection<String> getUsersForRole(Role role) {
        Map<String, String> uidMap = getUsers().get(role.toString().toLowerCase());
        if (uidMap == null) return new ArrayList<>();

        return uidMap.values();
    }

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

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public EventTicketingConfiguration getTicketingConfiguration() {
        return ticketingConfiguration;
    }

    // TODO put setters ??
}
