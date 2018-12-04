package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Spot;

public class EventMapEditionFragment extends EventMapFragment implements GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapEdition.STYLE_TAG";

    @Override
    protected void setUpCluster() {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            Objects.requireNonNull(getContext()), R.raw.map_edit_style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
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
            mMap.addMarker(new MarkerOptions().title(s.getTitle()).snippet(s.getSnippet())
                    .draggable(true).position(s.getPosition()));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
