package ch.epfl.sweng.eventmanager.repository.data;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class represents a geographic position, expressed as latitude and longitude
 * @author Louis Vialar
 */
public class Position {
    public static final Position EPFL = new Position(46.518510, 6.563249);

    /**
     * The latitude of the position
     */
    private double latitude;
    /**
     * The longitude of the position
     */
    private double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Position() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Create a new {@code LatLng} object with this position coordinates
     * @return a new LatLng object
     */
    public LatLng asLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return other.getLongitude() == getLongitude() && other.getLatitude() == getLatitude();
        } else return false;
    }
}
