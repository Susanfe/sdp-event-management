package ch.epfl.sweng.eventmanager.repository.data.MapEditionData;

import androidx.annotation.Nullable;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

/**
 * Class describing the tag of a marker
 */
public class EventEditionTag {

    private final int id;
    private final SpotType spotType;
    private final MarkerType markerType;

    /**
     * Creates an EventEditionTag with spot-like parameters
     * @param id marker id
     * @param spotType type of the marker
     * @return spot-like EventEditionTag
     */
    public static EventEditionTag createSpotTag(int id, SpotType spotType) {
        if (spotType == null) throw new IllegalArgumentException();
        return new EventEditionTag(id, spotType, MarkerType.SPOT);
    }

    /**
     * Creates an EventEditionTag with overlay-edge-like parameters
     * @param id marker id
     * @return overlay-edge-like EventEditionTag
     */
    public static EventEditionTag createOverlayEdgeTag(int id) {
        return new EventEditionTag(id, null, MarkerType.SPOT);
    }

    private EventEditionTag(int id, SpotType spotType, MarkerType markerType) {
        this.id = id;
        this.spotType = spotType;
        this.markerType = markerType;
    }

    public int getId() {
        return id;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public MarkerType getMarkerType() {
        return markerType;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof EventEditionTag) {
            EventEditionTag other = (EventEditionTag) obj;
            return other.getId() == getId()
                    && other.getMarkerType() == getMarkerType()
                    && other.getSpotType() == getSpotType();
        }

        return false;
    }
}
