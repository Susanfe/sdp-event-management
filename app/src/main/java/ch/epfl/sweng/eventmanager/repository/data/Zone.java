package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Color;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.List;

/**
 * This class represents a delineated geographical zone that is overlayed by a blue color using the
 * addPolygon method.
 *
 * This zone is delineated using a list of geographical positions, that can be accessed using
 * the getPositions method.
 */
public class Zone {
    private List<Position> positions;
    private static final int OVERLAY_INSIDE_COLOR = Color.argb(33, 24, 149, 244);

    /**
     * constructor
     *
     * @param positions that delineated the zone
     */
    public Zone(List<Position> positions) {
        this.positions = positions;
    }

    /**
     * empty contructor needed for firebase
     */
    public Zone(){}

    /**
     * @return A PolygonOptions instance that represents the overlay
     */
    public PolygonOptions addPolygon() {
        PolygonOptions options = new PolygonOptions()
                .fillColor(OVERLAY_INSIDE_COLOR)
                .strokeWidth(3)
                .strokeColor(Color.BLUE);
        for(int i = 0; i < positions.size(); ++i) {
            options.add(positions.get(i).asLatLng());
        }
        return options;
    }

    /**
     * @return the geographical positions that delineated the zone
     */
    public List<Position> getPositions() {
        return positions;
    }
}
