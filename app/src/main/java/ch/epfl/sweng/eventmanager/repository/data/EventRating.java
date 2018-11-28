package ch.epfl.sweng.eventmanager.repository.data;

public class EventRating {
    private String deviceId;
    private float rating;
    private String description;

    public EventRating() {
    }

    public EventRating(String deviceId, float rating, String description) {
        this.deviceId = deviceId;
        this.rating = rating;
        this.description = description;
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
}
