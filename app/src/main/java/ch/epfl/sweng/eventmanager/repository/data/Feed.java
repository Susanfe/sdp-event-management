package ch.epfl.sweng.eventmanager.repository.data;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Feed {
    private String id = "";
    private String content = "";
    //private String url = "";
    private String description = "";
    private Date time;
    //private String author = "";

    public Feed(JSONObject object) {
        try {
            String dateStr = object.getString("created_time");
            time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateStr);
            id = object.getString("id");
            /*
            if (object.has("full_picture")) {
                url = object.getString("full_picture");
            }
            */
            /*
            if (object.has("description")) {
                description = object.getString("description");
            }
            */
            if (object.has("message")) {
                content = object.getString("message");
            }
            /*
            if (object.has("name")) {
                author = object.getString("name");
            }
            */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feed)) return false;

        Feed that = (Feed) o;
        if(!id.equals(that.id)) return false;
        if(!time.equals(that.time)) return false;
        if(!content.equals(that.content)) return false;
        return true;
    }

}
