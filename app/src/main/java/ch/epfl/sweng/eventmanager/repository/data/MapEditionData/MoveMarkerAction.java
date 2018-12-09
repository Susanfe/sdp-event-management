package ch.epfl.sweng.eventmanager.repository.data.MapEditionData;

import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Objects;

public class MoveMarkerAction extends MapEditionAction {

    private final LatLng ancientPos;
    private final LatLng pos;

    public MoveMarkerAction(EventEditionTag markerTag, LatLng ancientPosition, LatLng actualPosition) {
        if (markerTag == null || ancientPosition == null || actualPosition == null)
            throw new IllegalArgumentException();

        this.tag = markerTag;
        this.ancientPos = ancientPosition;
        this.pos = actualPosition;
    }

    @Override
    public boolean revert(List<Marker> markers, SparseArray<LatLng> positions) {
        Marker marker = findMarkerByTag(markers);
        if (marker != null && marker.getPosition().equals(pos)) {
            marker.setPosition(ancientPos);
            positions.put(
                    ((EventEditionTag)Objects.requireNonNull(marker.getTag())).getId(), ancientPos);
            return true;
        }

        return false;
    }
}
