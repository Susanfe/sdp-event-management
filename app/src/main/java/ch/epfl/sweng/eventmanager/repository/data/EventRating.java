package ch.epfl.sweng.eventmanager.repository.data;

public class EventRating {
    private Float rating;
    private String description;

    public EventRating(Float rating, String description) {
        this.rating = rating;
        this.description = description;
    }

    public Float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
