package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomAddOptionsDialog;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomMarkerDialog;

public class EventMapEditionFragment extends EventMapFragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    // Tag for the style setting error
    private static final String TAG = "MapEdition.STYLE_TAG";

    // Tags for the created dialogs
    private static final String MARKER_DIALOG_TAG = "ui.event.interaction.fragments.EventMapEditionFragment.MARKER_DIALOG";
    private static final String CREATION_MARKER_TAG = "ui.event.interaction.fragments.EventMapEditionFragment.CREATION_MARKER";

    // Tags for the dialog-fragment communication (cf onActivityResults below)
    public static final int ADD_SPOT = 1;
    public static final int ADD_OVERLAY_EDGE = 2;
    private static final int ADD_REQUEST_CODE = 3;

    // Saved LatLng for the result of CustomAddOptionsDialog
    private LatLng onLongClickSavedLatLng = null;

    /**
     * Method is here used to do all the additional work without overriding onCreate(..)
     * No cluster is wanted in the edition version
     */
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
        mMap.setOnMapLongClickListener(this);
        addMarkers();
    }

    /**
     * Adds all the Spot objects contained in the database for this event
     */
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

    /**
     * Adds all spots to the map
     * @param spots List of Spot object to add to the map
     */
    private void addItems(List<Spot> spots) {
        for (Spot s : spots) {
            mMap.addMarker(new MarkerOptions().title(s.getTitle()).snippet(s.getSnippet())
                    .draggable(true).position(s.getPosition()));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        DialogFragment dialogFragment = new CustomMarkerDialog();
        dialogFragment.show(getChildFragmentManager(), MARKER_DIALOG_TAG);
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        onLongClickSavedLatLng = latLng;
        DialogFragment dialogFragment = new CustomAddOptionsDialog();
        dialogFragment.setTargetFragment(this, ADD_REQUEST_CODE);

        // Depending on SDK version, we need to use a different fragmentManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction().add(dialogFragment, CREATION_MARKER_TAG).commit();
        else
            getChildFragmentManager().beginTransaction().add(dialogFragment, CREATION_MARKER_TAG).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The method is called by the AddOptionDialog
        if (requestCode==ADD_REQUEST_CODE) {
            switch(resultCode) {

                // User chose to add a spot
                case ADD_SPOT:
                    addSpot(onLongClickSavedLatLng);
                    break;

                // User chose to add an overlay edge
                case ADD_OVERLAY_EDGE:
                    addOverlayEdge(onLongClickSavedLatLng);
                    break;
            }
        }
    }

    private void addSpot(LatLng onLongClickSavedLatLng) {
        DialogFragment createSpotDialog = new CustomMarkerDialog();
        createSpotDialog.show(getChildFragmentManager(), CREATION_MARKER_TAG);
    }

    private void addOverlayEdge(LatLng onLongClickSavedLatLng) {
    }




}
