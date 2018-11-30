package ch.epfl.sweng.eventmanager.repository.data;

import java.util.Date;

public class EventRating {
    private String deviceId;
    private float rating;
    private String description;
    private Date date;

    public EventRating() {
    }

    public EventRating(String deviceId, float rating, String description, long date) {
        this.deviceId = deviceId;
        this.rating = rating;
        this.description = description;
        this.date = new Date(date);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
