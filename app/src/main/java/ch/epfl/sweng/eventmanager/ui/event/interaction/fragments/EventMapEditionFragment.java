package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Spot;

public class EventMapEditionFragment extends EventMapFragment implements GoogleMap.OnMarkerClickListener {

    @Override
    protected void setUpCluster() {
        mMap.setOnMarkerClickListener(this);
        addMarkers();
    }

    private void addMarkers() {
        if (getActivity() != null){
            this.scheduleViewModel.getScheduledItems().observe(getActivity(), items ->
                    this.spotsModel.getSpots().observe(getActivity(), spots -> {
                        if (spots == null) {
                            return;
                        }
                        // Add new spots
                        for (Spot s : spots) {
                            s.setScheduleList(items);
                        }
                        addItems(spots);
                    }));

        }
    }

    private void addItems(List<Spot> spots) {
        for (Spot s : spots) {
            mMap.addMarker(new MarkerOptions().title(s.getTitle()).snippet(s.getSnippet()).draggable(true));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
