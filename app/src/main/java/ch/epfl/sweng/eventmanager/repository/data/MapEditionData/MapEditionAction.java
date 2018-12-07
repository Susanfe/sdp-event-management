package ch.epfl.sweng.eventmanager.repository.data.MapEditionData;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * Describes a simple edition action and its ability to be reverted
 */
public abstract class MapEditionAction {

    // Tag with which to find marker
    protected MarkerType tag;

    /**
     * Method that allows reverting the action
     * @param markers list of the markers on the map to find marker on which the action was done
     */
    public abstract boolean revert(List<Marker> markers);

    /**
     * Finds the marker with the same tag as the one with the tag registered for the action
     * @param list list of markers on the map
     * @return the first marker with the matching tag, null otherwise
     */
    Marker findMarkerByTag(List<Marker> list) {
        for (Marker m : list) {
            if (m.getTag() == tag) return m;
        }

        return null;
    }
}
