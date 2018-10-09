package ch.epfl.sweng.eventmanager.repository.data;

/**
 * Represents the place where an event takes place
 *
 * @author Louis Vialar
 */
public class EventLocation {
    /**
     * The public name for the location (e.g. EPFL)
     */
    private String name;
    /**
     * The latitude of the location
     */
    private double latitude;
    /**
     * The longitude of the location
     */
    private double longitude;

    public EventLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public EventLocation() {
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
