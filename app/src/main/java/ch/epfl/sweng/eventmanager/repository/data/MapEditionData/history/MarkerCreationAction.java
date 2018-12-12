package ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history;

import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.EventEditionTag;

public class MarkerCreationAction extends MapEditionAction {

    private LatLng positionofCreation;

    public MarkerCreationAction(EventEditionTag tag, LatLng position) {
        if (tag == null || position == null)
            throw new IllegalArgumentException();

        this.tag = tag;
        this.positionofCreation = position;
    }

    @Override
    public boolean revert(List<Marker> markers, SparseArray<LatLng> positions) {
        Marker m = findMarkerByTag(markers);
        if (m != null && m.getPosition().equals(positionofCreation)) {
            markers.remove(m);
            m.remove();
            return true;
        }

        return false;
    }
}
