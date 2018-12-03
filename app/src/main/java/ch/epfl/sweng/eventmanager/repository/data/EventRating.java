package ch.epfl.sweng.eventmanager.repository.data;

public class EventRating {
    private String deviceId;
    private float rating;
    private String description;
    private long date;

    public EventRating() {
    }

    public EventRating(String deviceId, float rating, String description, long date) {
        this.deviceId = deviceId;
        this.rating = rating;
        this.description = description;
        this.date = date;
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

    public long getDate() {
        return date;
    }
}
