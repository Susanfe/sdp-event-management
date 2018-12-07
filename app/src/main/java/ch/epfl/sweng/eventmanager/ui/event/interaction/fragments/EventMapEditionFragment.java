package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;
import java.util.Stack;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.DialogFragment;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.MapEditionAction;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.MarkerType;
import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomAddOptionsDialog;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomMarkerDialog;

public class EventMapEditionFragment extends EventMapFragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

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
    private Stack<MapEditionAction> history = new Stack<>();

    /**
     * Method is here used to do all the additional work without overriding onCreate(..)
     * No clustering is wanted in the edition version
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

        // Set Listeners
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        addMarkers();

        // Add markers for overlay edge positions
        float[] overlayHSL = new float[3];
        ColorUtils.colorToHSL(getResources().getColor(R.color.overlay_blue), overlayHSL);
        overlayEdgeOnMap(overlayHSL[0]);

    }

    /**
     * Adds all the Spot objects contained in the database for this event at the initialization
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
                            addSpotMarker(s.getTitle(), s.getSnippet(), s.getPosition());
                        }
                    }));

        }
    }

    /**
     * Adds a spotType marker to the map
     * @param title title of the marker
     * @param snippet snippet of the marker
     * @param position LatLng object representing marker's position
     */
    private void addSpotMarker(String title, String snippet, LatLng position) {
            Marker m = mMap.addMarker(new MarkerOptions().title(title).snippet(snippet)
                    .draggable(true).position(position));
            m.setTag(MarkerType.SPOT);
    }

    /**
     * Adds all overlay edges to the map at the initialization
     * @param hue float value representing the hue (color) to be applied to the marker
     */
    private void overlayEdgeOnMap(float hue) {
        if (getActivity() != null){
            this.zonesModel.getZone().observe(getActivity(), zones -> {
                if (zones != null) {
                    for (Zone z : zones) {
                        for (Position p : z.getPositions()){
                            addOverlayEdgeMarker(p.asLatLng(), hue);
                        }
                    }
                }
            });
        }
    }

    private void addOverlayEdgeMarker(LatLng position, float hue) {
        Marker m = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(hue))
                .position(position).draggable(true));

        m.setTag(MarkerType.OVERLAY_EDGE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        MarkerType markerType = (MarkerType) marker.getTag();
        if (markerType!=null)
            switch (markerType) {
                case SPOT:
                    DialogFragment dialogFragment = new CustomMarkerDialog();
                    dialogFragment.show(getChildFragmentManager(), MARKER_DIALOG_TAG);
                    return true;

                case OVERLAY_EDGE:
                    return true;
            }
        return false;
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
                    addSpotEvent(onLongClickSavedLatLng);
                    break;

                // User chose to add an overlay edge
                case ADD_OVERLAY_EDGE:
                    addOverlayEdgeEvent(onLongClickSavedLatLng);
                    break;
            }
        }
    }

    /**
     * Handles adding a spotType marker event
     * @param onLongClickSavedLatLng LatLng object representing position to create marker at
     */
    private void addSpotEvent(LatLng onLongClickSavedLatLng) {
        DialogFragment createSpotDialog = new CustomMarkerDialog();
        createSpotDialog.show(getChildFragmentManager(), CREATION_MARKER_TAG);
    }

    /**
     * Adds an OverlayEdge type marker at the specufied position
     * @param onLongClickSavedLatLng LatLng object representing position to create marker at
     */
    private void addOverlayEdgeEvent(LatLng onLongClickSavedLatLng) {
    }

    /*
     * The three following methods are for the onMapMarkerDragListener
     */
    @Override
    public void onMarkerDragStart(Marker marker) { }

    @Override
    public void onMarkerDrag(Marker marker) { }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // TODO implement saving the new position of the marker into Firebase
    }
}
