package ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history;

import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.EventEditionTag;

/**
 * Describes a simple edition action and its ability to be reverted
 */
public abstract class MapEditionAction {

    // Tag with which to find marker
    protected EventEditionTag tag;

    /**
     * Method that allows reverting the action
     * @param markers list of the markers on the map to find marker on which the action was done
     */
    public abstract boolean revert(List<Marker> markers, SparseArray<LatLng> positions);

    /**
     * Finds the marker with the same tag as the one with the tag registered for the action
     * @param list list of markers on the map
     * @return the first marker with the matching tag, null otherwise
     */
    Marker findMarkerByTag(List<Marker> list) {
        for (Marker m : list) {
            EventEditionTag tag = (EventEditionTag) m.getTag();
            if (tag != null && tag.equals(this.tag))
                return m;
        }

        return null;
    }
}
