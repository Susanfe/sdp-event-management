package ch.epfl.sweng.eventmanager.repository.data;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import ch.epfl.sweng.eventmanager.inject.GlideApp;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseHelper;
import ch.epfl.sweng.eventmanager.users.Role;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
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
     * An internal id identifying this event uniquely.
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
     * An URL to the image representing the event stored in Firebase Storage, may be null.
     * We store a string because Firebase can't handle URI objects
     */
    private String imageURL;
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

    /**
     * If true, then the event can be seen and accessed by any user. Otherwise, if false, it can only be seen by
     * the administrators for this event
     */
    private boolean visibleFromPublic;

    private EventTicketingConfiguration ticketingConfiguration;

    // TODO define if an event can have only empty and null atributes
    public Event(int id, String name, String description, Date beginDate, Date endDate, String organizerEmail,
                 Uri imageURL, EventLocation location, Map<String, String> users, String twitterName,
                 String facebookName) {
        this(id, name, description, beginDate, endDate, organizerEmail, imageURL, location, users, twitterName,
                facebookName, null);
    }

    public Event(int id, String name, String description, Date beginDate, Date endDate, String organizerEmail,
                 Uri imageURL, EventLocation location, Map<String, String> users, String twitterName,
                 String facebookName, EventTicketingConfiguration ticketingConfiguration) {

        this.ticketingConfiguration = ticketingConfiguration;

        if (beginDate.getTime() > endDate.getTime())
            throw new IllegalArgumentException("The time at the start of the event should be later than the time at " +
                    "the end");

        this.id = id;
        this.name = name;
        this.beginDate = beginDate.getTime();
        this.endDate = endDate.getTime();
        this.description = description;
        this.organizerEmail = organizerEmail;
        this.imageURL = imageURL == null ? null : imageURL.toString();
        this.location = location;
        this.users = users;
        this.twitterName = twitterName;
        this.facebookName = facebookName;
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public EventLocation getLocation() {
        return location;
    }

    /**
     * Return true if the event is visible for any user. Otherwise, if false, the event can only be accessed
     * by declared administrators for this event
     *
     * @return true if event is visible
     */
    public boolean isVisibleFromPublic() {
        return visibleFromPublic;
    }

    /**
     * Allows to change the visibility of the event
     *
     * @param visibleFromPublic
     */
    public void setVisibleFromPublic(boolean visibleFromPublic) {
        this.visibleFromPublic = visibleFromPublic;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    @Exclude
    public Uri getImageURLasURI() {
        return haveAnImage() ? Uri.parse(imageURL) : null;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public List<String> getUsersForRole(Role role) {
        return getPermissions().get(role);
    }

    public String getTwitterName() {
        return this.twitterName;
    }

    public String getFacebookName() {
        return this.facebookName;
    }


    /**
     * Do not use, only public due to a limitation of our auto-matching with Firebase.
     *
     * @return firebase representation of user permissions.
     */
    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

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
            if (result.get(role) == null) {
                users = new ArrayList<>();
                users.add(uid);
                result.put(role, users);
            } else {
                result.get(role).add(uid);
            }
        }

        return result;
    }

    // FIXME Use or delete method ?

    public String beginDateAsString() {
        if (beginDate <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        return f.format(beginDate);
    }

    public String endDateAsString() {
        if (endDate <= 0) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        return f.format(endDate);
    }

    public EventTicketingConfiguration getTicketingConfiguration() {
        return ticketingConfiguration;
    }

    public void setTicketingConfiguration(EventTicketingConfiguration ticketingConfiguration) {
        this.ticketingConfiguration = ticketingConfiguration;
    }

    @Exclude
    public boolean haveAnImage() {
        return imageURL != null;
    }


    @Exclude
    public void uploadImage(Uri imgSrc) {
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("events-logo");
        StorageReference eventsLogoRef = imagesRef.child(getImageName());
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference("events").child(String.valueOf(getId()));
        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/webp").build();
        FirebaseHelper.uploadFileToStorage(eventsLogoRef, imgSrc, metadata).addOnSuccessListener(taskSnapshot -> eventsLogoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            setImageURL(uri.toString());
            Map<String, Object> updateUrl = new HashMap<>();
            updateUrl.put("imageURL", uri.toString());
            dbRef.updateChildren(updateUrl);
        }));
    }

    /**
     * Return the name for storing the eventImage into firebase
     *
     * @return String imageName
     */
    @Exclude
    private String getImageName() {
        return this.getId() + ".webp";
    }

    /**
     * Will load the event image into the provided view
     *
     * @param context
     * @param imageView
     */
    @Exclude
    public void loadEventImageIntoImageView(Context context, ImageView imageView) {
        if (haveAnImage()) {
            CircularProgressDrawable progress = new CircularProgressDrawable(context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();
            GlideApp.with(context).load(getImageURLasURI()).placeholder(progress).into(imageView);
        }
    }

    /**
     * Will load the event image into the provided view and apply the requested transformation
     *
     * @param context
     * @param imageView
     * @param transformation
     */
    @Exclude
    public void loadEventImageIntoImageView(Context context, ImageView imageView, BitmapTransformation transformation) {
        if (haveAnImage()) {
            CircularProgressDrawable progress = new CircularProgressDrawable(context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();
            GlideApp.with(context).load(getImageURLasURI()).apply(RequestOptions.bitmapTransform(transformation)).placeholder(progress).into(imageView);
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
