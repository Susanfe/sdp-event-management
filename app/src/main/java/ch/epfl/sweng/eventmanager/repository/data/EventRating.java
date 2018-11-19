package ch.epfl.sweng.eventmanager.repository.data;

public class EventRating {
    private float rating;
    private String description;

    public EventRating(float rating, String description) {
        this.rating = rating;
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
