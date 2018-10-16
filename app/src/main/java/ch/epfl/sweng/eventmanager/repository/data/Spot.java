package ch.epfl.sweng.eventmanager.repository.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Represents a place into the event
 *
 * @author Robin Zbinden (274236)
 * @author Stanislas Jouven (260580)
 */
public class Spot implements ClusterItem {

    /**
     * The name for the spot
     */
    private String title;
    /**
     * The description of the spot
     */
    private SpotType spotType;
    /**
     * The position of the spot
     */
    private LatLng position;

    public Spot(String title, SpotType spotType, double latitude, double longitude) {
        this.title = title;
        this.spotType = spotType;
        this.position = new LatLng(latitude, longitude);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return spotType.getName();
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
