package ch.epfl.sweng.eventmanager.repository.data;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import ch.epfl.sweng.eventmanager.inject.GlideApp;
import ch.epfl.sweng.eventmanager.users.Role;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.Exclude;
import jp.wasabeef.glide.transformations.BitmapTransformation;

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
    private String organizerEmail;
    /**
     * An URI to the image representing the event, may be null
     */
    private Uri imageURI;
    /**
     * The location of the event
     */
    private EventLocation location;

    /**
     * A map from roles to a list of user UIDs.
     */
    private Map<String, String> users;

    /**
     * The twitter account screen name
     */
    private String twitterName;

    /**
     * The facebook account screen name
     */
    private String facebookName;

    private EventTicketingConfiguration ticketingConfiguration;

    // TODO define if an event can have only empty and null atributes
    public Event(int id, String name, String description, Date beginDate, Date endDate,
                 String organizerEmail, Uri imageURI, EventLocation location,
                 Map<String, String> users, String twitterName, String facebookName) {
        this(id, name, description, beginDate, endDate, organizerEmail, imageURI, location, users, twitterName, facebookName, null);
    }

    public Event(int id, String name, String description, Date beginDate, Date endDate,
                 String organizerEmail, Uri imageURI, EventLocation location,
                 Map<String, String> users, String twitterName, String facebookName, EventTicketingConfiguration ticketingConfiguration) {

        this.ticketingConfiguration = ticketingConfiguration;

        if (beginDate.getTime() > endDate.getTime())
            throw new IllegalArgumentException("The time at the start of the event should be later than the time at the end");

        this.id = id;
        this.name = name;
        this.beginDate = beginDate.getTime();
        this.endDate = endDate.getTime();
        this.description = description;
        this.organizerEmail = organizerEmail;
        this.imageURI = imageURI;
        this.location = location;
        this.users = users;
        this.twitterName = twitterName;
        this.facebookName = facebookName;
    }

    public Event() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getBeginDate() { return beginDate; }

    public long getEndDate() {return endDate;}

    public String getDescription() {
        return description;
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public EventLocation getLocation() {
        return location;
    }

    /**
     * Do not use, only public due to a limitation of our auto-matching with Firebase.
     *
     * @return firebase representation of user permissions.
     */
    public Map<String, String> getUsers() { return users; }

    /**
     * @return a map of Role permissions to User IDs
     */
    @Exclude
    public Map<Role, List<String>> getPermissions() {
        Map<Role, List<String>> result = new HashMap<>();

        // Don't blow up if the event does not contain any permission data
        if (getUsers() == null) return result;

        // The keys of a Java Map are unique
        for (String uid : getUsers().keySet()) {
            Role role = Role.valueOf(getUsers().get(uid).toUpperCase());

            List<String> users;
            if (result.get(role) == null) users = Collections.singletonList(uid);
            else users = result.get(role);

            result.put(role, users);
        }

        return result;
    }

    // FIXME Use or delete method ?
    public List<String> getUsersForRole(Role role) {
        return getPermissions().get(role);
    }

    public String getTwitterName() {
        return this.twitterName;
    }

    public String getFacebookName() {
        return this.facebookName;
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

    public void setImageURL(Uri imageURI) {
        this.imageURI = imageURI;
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

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public void setTicketingConfiguration(EventTicketingConfiguration ticketingConfiguration) {
        this.ticketingConfiguration = ticketingConfiguration;
    }


    @Exclude
    public Uri getImageURI() {
        return imageURI;
    }

    @Exclude
    public boolean haveAnImage() {
        return imageURI != null;
    }

    /**
     * Will load the event image into the provided view
     * @param context
     * @param imageView
     */
    @Exclude
    public void loadEventImageIntoImageView(Context context, ImageView imageView) {
        if(getImageURI() != null) {
            CircularProgressDrawable progress = new CircularProgressDrawable(context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();
            GlideApp.with(context).load(getImageURI()).placeholder(progress).into(imageView);
        }
    }

    /**
     * Will load the event image into the provided view and apply the requested transformation
     * @param context
     * @param imageView
     * @param transformation
     */
    @Exclude
    public void loadEventImageIntoImageView(Context context, ImageView imageView, BitmapTransformation transformation) {
        if (getImageURI() != null) {
            CircularProgressDrawable progress = new CircularProgressDrawable(context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();
            GlideApp.with(context).load(getImageURI()).apply(RequestOptions.bitmapTransform(transformation)).placeholder(progress).into(imageView);
        }
    }

    @Exclude
    public Date getBeginDateAsDate() {
        if (beginDate <= 0) {
            return null;
        }
        return new Date(beginDate);
    }

    @Exclude
    public Date getEndDateAsDate() {
        if (endDate <= 0) {
            return null;
        }
        return new Date(endDate);
    }

}
