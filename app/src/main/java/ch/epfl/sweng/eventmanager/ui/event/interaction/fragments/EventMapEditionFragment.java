package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.DialogFragment;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.MapEditionAction;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.MarkerType;
import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;
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
    private static final int ADD_OVERLAY_OR_SPOT_REQUEST_CODE = 3;
    private static final int ADD_SPOT_REQUEST_CODE = 4;
    public static final int SPOT_INFO_EDITION = 5;

    // Saved LatLng for the result of CustomAddOptionsDialog
    private LatLng onLongClickSavedLatLng = null;
    private int onClickSavedMarkerID = 0;

    // Counters for each MarkerType
    private int counterSpot = 0;
    private int counterOverlayEdge = 0;

    // List of all added markers
    private List<Marker> markerList = new LinkedList<>();

    // History of actions on the markers
    private Stack<MapEditionAction> history = new Stack<>();

    // Utils
    private float hueOverlayBlue;
    private static final String default_title = "New Spot";
    private static final String default_snippet = "Amazing description";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.menu_map_edition_save).setVisible(true);
        menu.findItem(R.id.menu_map_edition_help).setVisible(true);
        menu.findItem(R.id.menu_map_edition_undo).setVisible(true);
        menu.findItem(R.id.menu_map_edition_edit).setVisible(false);
    }

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
        hueOverlayBlue = overlayHSL[0];
        overlayEdgeOnMap();

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

            m.setTag(MarkerType.SPOT.setId(++counterSpot));
            onClickSavedMarkerID = counterSpot;
            markerList.add(m);
    }

    /**
     * Adds all overlay edges to the map at the initialization
     */
    private void overlayEdgeOnMap() {
        if (getActivity() != null){
            this.zonesModel.getZone().observe(getActivity(), zones -> {
                if (zones != null) {
                    for (Zone z : zones) {
                        for (Position p : z.getPositions()){
                            addOverlayEdgeMarker(p.asLatLng());
                        }
                    }
                }
            });
        }
    }

    private void addOverlayEdgeMarker(LatLng position) {
        Marker m = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(hueOverlayBlue))
                .position(position).draggable(true));

        m.setTag(MarkerType.OVERLAY_EDGE.setId(++counterOverlayEdge));
        markerList.add(m);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        MarkerType markerType = (MarkerType) marker.getTag();
        if (markerType!=null)
            switch (markerType) {
                case SPOT:
                    onClickSavedMarkerID = markerType.getId();
                    DialogFragment dialogFragment = new CustomMarkerDialog();
                    dialogFragment.setTargetFragment(this, ADD_SPOT_REQUEST_CODE);
                    showDialogFragment(dialogFragment, MARKER_DIALOG_TAG);
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
        dialogFragment.setTargetFragment(this, ADD_OVERLAY_OR_SPOT_REQUEST_CODE);

        showDialogFragment(dialogFragment, CREATION_MARKER_TAG);
    }

    /**
     * Displays the FragmentDialog, code varies according to sdk versions
     * @param dialogFragment DialogFragment object to display
     * @param tag String tag to identify the fragment transaction
     */
    private void showDialogFragment(DialogFragment dialogFragment, String tag) {
        // Depending on SDK version, we need to use a different fragmentManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction().add(dialogFragment, tag).commit();
        else
            getChildFragmentManager().beginTransaction().add(dialogFragment, tag).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The method is called by the AddOptionDialog
        if (requestCode== ADD_OVERLAY_OR_SPOT_REQUEST_CODE) {
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

        } else if (requestCode== ADD_SPOT_REQUEST_CODE) {
        // Set the marker to its new infos
            String title = data.getStringExtra(CustomMarkerDialog.EXTRA_TITLE);
            String snippet = data.getStringExtra(CustomMarkerDialog.EXTRA_SNIPPET);
            SpotType type = data.getParcelableExtra(CustomMarkerDialog.EXTRA_TYPE);

            Marker m = findMarkerById(onClickSavedMarkerID);
            if (m!=null) {
                m.setTitle(title);
                m.setSnippet(snippet);
                // TODO find way to change spot type as marker does not hold info (enum?)
            }
        }
    }

    /**
     * Finds marker with its id among all the placed markers
     * @param onClickSavedMarkerID id of the marker to find
     * @return marker with corresponding id or null if none
     */
    private Marker findMarkerById(int onClickSavedMarkerID) {
        for (Marker m : markerList) {
            if (m.getTag() == MarkerType.SPOT &&
                    ((MarkerType)Objects.requireNonNull(m.getTag())).getId() == onClickSavedMarkerID)
                return m;
        }
        return null;
    }

    /**
     * Handles adding a spotType marker event
     */
    private void addSpotEvent(LatLng onLongClickSavedLatLng) {
        addSpotMarker(default_title, default_snippet, onLongClickSavedLatLng);
        DialogFragment createSpotDialog = new CustomMarkerDialog();
        createSpotDialog.setTargetFragment(this, ADD_SPOT_REQUEST_CODE);
        showDialogFragment(createSpotDialog, MARKER_DIALOG_TAG);
        // TODO add matching history actions
    }

    /**
     * Adds an OverlayEdge type marker at the specufied position
     * @param onLongClickSavedLatLng LatLng object representing position to create marker at
     */
    private void addOverlayEdgeEvent(LatLng onLongClickSavedLatLng) {
        addOverlayEdgeMarker(onLongClickSavedLatLng);
        // TODO link to other overlay edges and reform polygon
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
