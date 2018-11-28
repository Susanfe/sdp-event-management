package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.eventmanager.users.Role;
import com.google.firebase.database.Exclude;


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
    private String organizerEmail;
    /**
     * An image representing the event, may be null
     */
    private Bitmap image;
    /**
     * The location of the event
     */
    private EventLocation location;

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
                 String organizerEmail, Bitmap image, EventLocation location,
                 Map<String, Map<String, String>> users, String twitterName) {
        this(id, name, description, beginDate, endDate, organizerEmail, image, location, users, twitterName, null);
    }

    public Event(int id, String name, String description, Date beginDate, Date endDate,
                 String organizerEmail, Bitmap image, EventLocation location,
                 Map<String, Map<String, String>> users, String twitterName, EventTicketingConfiguration ticketingConfiguration) {
        this.ticketingConfiguration = ticketingConfiguration;

        if (beginDate.getTime() > endDate.getTime())
            throw new IllegalArgumentException("The time at the start of the event should be later than the time at the end");

        this.id = id;
        this.name = name;
        this.beginDate = beginDate.getTime();
        this.endDate = endDate.getTime();
        this.description = description;
        this.organizerEmail = organizerEmail;
        this.image = image;
        this.location = location;
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

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    @Exclude
    public Bitmap getImage() {
        return image;
    }

    public EventLocation getLocation() {
        return location;
    }

    /**
     * Do not use, only public due to a limitation of our auto-matching with Firebase.
     *
     * @return firebase representation of user permissions.
     */
    public Map<String, Map<String, String>> getUsers() { return users; }

    /**
     * @return a map of Role permissions to User IDs
     */
    @Exclude
    public Map<Role, List<String>> getPermissions() {
        Map<Role, List<String>> result = new HashMap<>();

        // Don't blow up if the event does not contain any permission data
        if (getUsers() == null) return result;


        // The keys of a Java Map are unique
        for (String rawRole : getUsers().keySet()) {
            Role role = Role.valueOf(rawRole.toUpperCase());
            // TODO handle null pointer exception
            List<String> users = new ArrayList<>(getUsers().get(rawRole).values());

            result.put(role, users);
        }

        return result;
    }

    // FIXME Use or delete method ?
    public Collection<String> getUsersForRole(Role role) {
       Map<String, String> uidMap = getUsers().get(role.toString().toLowerCase());
       if (uidMap == null) return new ArrayList<>();
       return uidMap.values();
    }

    public String getTwitterName() {
        return this.twitterName;
    }

    String beginDateAsString() {
        if (beginDate <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        return f.format(beginDate);
    }

    String endDateAsString(){
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public void setUsers(Map<String, Map<String, String>> users) {
        this.users = users;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public void setTicketingConfiguration(EventTicketingConfiguration ticketingConfiguration) {
        this.ticketingConfiguration = ticketingConfiguration;
    }

    // TODO put setters ??
}
