package ch.epfl.sweng.eventmanager.repository.data;

/**
 * Represents a place into the event
 *
 * @author Robin Zbinden (274236)
 * @author Stanislas Jouven (260580)
 */
public class Spot {

    /**
     * The name for the spot
     */
    private String name;
    /**
     * The description of the spot
     */
    private SpotType spotType;
    /**
     * The latitude of the location
     */
    private double latitude;
    /**
     * The longitude of the location
     */
    private double longitude;

    public Spot(String name, SpotType spotType, double latitude, double longitude) {
        this.name = name;
        this.spotType = spotType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
