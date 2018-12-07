package ch.epfl.sweng.eventmanager.repository.data.MapEditionData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MarkerCreationAction extends MapEditionAction {

    private LatLng positionofCreation;

    public MarkerCreationAction(MarkerType tag, LatLng position) {
        if (tag == null || position == null)
            throw new IllegalArgumentException();

        this.tag = tag;
        this.positionofCreation = position;
    }

    @Override
    public boolean revert(List<Marker> markers) {
        Marker m = findMarkerByTag(markers);
        if (m != null && m.getPosition() == positionofCreation) {
            m.setTag(MarkerType.TO_REMOVE);
            m.setVisible(false);
            return true;
        }

        return false;
    }
}
