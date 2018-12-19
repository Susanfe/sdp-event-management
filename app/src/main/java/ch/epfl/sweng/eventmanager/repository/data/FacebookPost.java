package ch.epfl.sweng.eventmanager.repository.data;

import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
This class enable to store every component(message, post time, author) of a facebook post.
 */
public class FacebookPost {
    private String id = "";
    private String content = "";
    private Date time;
    private String url = "";
    private String description = "";
    private String name = "";

    /**
     * Stores in class instances components of the facebook post
     *
     * @param object the JSONObject that contain all the information
     */
    public FacebookPost(JSONObject object) {
        try {
            String dateStr = object.getString("created_time");
            time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).parse(dateStr);
            id = object.getString("id");
            if (object.has("full_picture")) {
                url = object.getString("full_picture");
            }

            if (object.has("description")) {
                description = object.getString("description");
            }
            if (object.has("message")) {
                content = object.getString("message");
            }
            if (object.has("name")) {
                name = object.getString("name");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getTime() {
        return time;
    }

    /**
     *
     * @return the string representation of the facebook post time
     */
    public String dateAsString() {
        if (time.getTime() <= 0) {
            return null;
        }
        DateFormat f = DateFormat.getDateTimeInstance();
        return f.format(time.getTime());
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getName() { return name; }

    public Uri getImageURL() { return url != null ? Uri.parse(url) : null; }

    public String getDescription() { return description; }

    public boolean hasImage() {
        return (url != null && url.length() > 0);
    }

    public boolean hasName() {
        return (name != null && name.length() > 0);
    }

    public boolean hasDescription() {
        return (description != null && description.length() > 0);
    }

    public boolean hasContent() {
        return (content != null && content.length() > 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookPost)) return false;

        FacebookPost that = (FacebookPost) o;
        return (id.equals(that.id) && time.equals(that.time) && content.equals(that.content));
    }

}
