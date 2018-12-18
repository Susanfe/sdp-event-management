package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Color;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

import androidx.core.math.MathUtils;

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
     *  Creates a Zone from the initial positions as edges of the borderline
     * @param positions that delineated the zone
     */
    public Zone(List<Position> positions) {
        this.positions = positions;
    }

    /**
     * Empty contructor needed for firebase
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

        for (Position p : positions)
            options.add(p.asLatLng());

        return options;
    }

    /**
     * @return the geographical positions that delineated the zone
     */
    public List<Position> getPositions() {
        return positions;
    }

    /**
     * Adds a new position to the list of registered positions.
     * @param position Position to add to zone as one of its edges
     * @throws IllegalArgumentException if the given position is null
     */
    public void addPosition(Position position) {
        if (position != null) {
            int indexToPlace = computeIndexForPosition(position);
            positions.add(indexToPlace, position);
        } else throw new IllegalArgumentException("Trying to add null position!");
    }

    /**
     * Computes the pair of consecutive points to which the given position is the nearest.
     * @param position position to place in the array of positions.
     * @return the index of the first of the pair in the middle of which to insert the position.
     */
    private int computeIndexForPosition(Position position) {
        double min = Integer.MAX_VALUE;
        int minIndex = 0;
        int length = positions.size();
        for (int i = 0; i < length; i++) {
            double norm = computeDistance(position, positions.get(i%length),positions.get((i+1)%length));
            if (norm < min) {
                min = norm;
                minIndex = i;
            }
        }

        return (minIndex+1)%length;
    }

    /**
     * Computes the sum of the distances of one point to two other points.
     * @param pos the point to compute the distance to
     * @param firstPos other first point
     * @param secondPos other second point
     * @return the sum of their distances
     */
    private double computeDistance(Position pos, Position firstPos, Position secondPos) {
        double distance1 = norm(pos, firstPos);
        double distance2 = norm(pos, secondPos);

        return distance1+distance2;
    }

    /**
     * Computes the norm between position (two-dimensional) vectors.
     * @param p1 first point
     * @param p2 second point
     * @return distance between the two.
     */
    private double norm(Position p1, Position p2) {
        return Math.sqrt(
                (p1.getLongitude() - p2.getLongitude())*(p1.getLongitude() - p2.getLongitude()) +
                        (p1.getLatitude() - p2.getLatitude())*(p1.getLatitude() - p2.getLatitude()));
    }

    /**
     * Enables updating the position of one element on the list. The position is removed from the
     * list and an element with the new position is reinserted using the addPosition method.
     * @param ancientPosition position element to find
     * @param newPosition position element to insert in place of ancient one
     * @return false if no position was found for the given ancient position
     */
    public boolean changePositionOfElement(Position ancientPosition, Position newPosition) {
        if (ancientPosition == null || newPosition == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < positions.size(); i++) {
            Position p =positions.get(i);
            if (p.equals(ancientPosition)) {
                if (!positions.remove(p))
                    return false;
                positions.add(i, newPosition);
                return true;
            }
        }
        return false;
    }

    public boolean removePosition(Position position) {
        return positions.remove(position);
    }


}
