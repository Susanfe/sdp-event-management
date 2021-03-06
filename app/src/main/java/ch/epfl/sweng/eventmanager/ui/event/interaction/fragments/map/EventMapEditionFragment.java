package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import javax.inject.Inject;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.DialogFragment;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.MapEditionRepository;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.*;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history.MapEditionAction;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history.MarkerCreationAction;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history.ModifyMarkerInfoAction;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history.MoveMarkerAction;
import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomAddOptionsDialog;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomInfoDialog;
import ch.epfl.sweng.eventmanager.ui.CustomViews.CustomMarkerDialog;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import dagger.android.support.AndroidSupportInjection;

import static ch.epfl.sweng.eventmanager.repository.data.MapEditionData.EventEditionTag.createOverlayEdgeTag;
import static ch.epfl.sweng.eventmanager.repository.data.MapEditionData.EventEditionTag.createSpotTag;

/**
 * This fragment handles all map-related editing. It enables to move or create markers of any type, change
 * SPOT markers infos. This class pushes the edition on Firebase when user saves info.
 */
public class EventMapEditionFragment extends EventMapFragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    // Tag for the style setting error
    private static final String TAG = "MapEdition.STYLE_TAG";

    // Tags for the created dialogs
    private static final String MARKER_DIALOG_TAG = "ui.event.interaction.fragments.EventMapEditionFragment.MARKER_DIALOG";
    private static final String ADD_MARKER_DIALOG_TAG = "ui.event.interaction.fragments.EventMapEditionFragment.ADD_MARKER_DIALOG";
    private static final String HELP_DIALOG_TAG =  "ui.event.interaction.fragments.EventMapEditionFragment.HELP_DIALOG_TAG";

    // Tags for the dialog-fragment communication (cf onActivityResults below)
    public static final int ADD_SPOT = 1;
    public static final int ADD_OVERLAY_EDGE = 2;
    private static final int ADD_OVERLAY_OR_SPOT_REQUEST_CODE = 3;
    private static final int SPOT_INFO_REQUEST_CODE = 4;
    public static final int SPOT_INFO_EDITION = 5;

    // Saved LatLng for the result of marker add dialog
    private LatLng onLongClickSavedLatLng = null;
    // Saved marker id for the result of info modifying dialog
    private int onClickSavedMarkerID = 0;
    // Saved creationAction to link to modifyInfoAction on return of modifying dialog
    private boolean issuedByCreation;

    // Counters for each MarkerType
    private int counterSpot = 0;
    private int counterOverlayEdge = 0;

    // List of all added markers
    private List<Marker> markerList = new LinkedList<>();
    private SparseArray<LatLng> positionForMarkerIDMap = new SparseArray<>();
    private Zone eventZone = null;

    private static final int overlayEdgeIndexShift = 1000;

    // History of actions on the markers
    private Stack<MapEditionAction> history = new Stack<>();

    // Utils
    private float hueOverlayBlue;
    public static final String defaultTitle = "New Spot";
    public static final String defaultSnippet = "Amazing description";
    private final SpotType defaultType = SpotType.ROOM;

    @Inject
    protected MapEditionRepository repository;

    public static EventMapEditionFragment newInstance() {
        return new EventMapEditionFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.menu_showcase_activity_map_edition_save).setVisible(true);
        menu.findItem(R.id.menu_showcase_activity_map_edition_help).setVisible(true);
        menu.findItem(R.id.menu_showcase_activity_map_edition_undo).setVisible(true);
        menu.findItem(R.id.menu_showcase_activity_map_edition_edit).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_showcase_activity_map_edition_help:
                Bundle bundle = new Bundle();
                bundle.putString(CustomInfoDialog.CUSTOM_DIALOG_INFO_STRING, getString(R.string.map_edition_info_text));
                DialogFragment dialog = new CustomInfoDialog();
                dialog.setArguments(bundle);
                showDialogFragment(dialog, HELP_DIALOG_TAG);
                break;

            case R.id.menu_showcase_activity_map_edition_save:
                confirmSaveAndQuit(getEditedSpots());
                break;

            case R.id.menu_showcase_activity_map_edition_undo:
                if (history.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.undo_empty), Toast.LENGTH_SHORT).show();
                else revertLatestAction();

                return true;

        }

        return false;
    }

    /**
     * Collects every spot that the admin wants to display on the event map.
     * This is dine by iterating over the markers and adding Spot object created out of SPOT markers
     * @return the list of Spots the user wants for his event.
     */
    private List<Spot> getEditedSpots() {
        List<Spot> result = new LinkedList<>();
        for (Marker m : markerList) {
            EventEditionTag tag = (EventEditionTag) m.getTag();
            if (tag != null && tag.getMarkerType() == MarkerType.SPOT) {
                LatLng pos = m.getPosition();
                result.add(new Spot(m.getTitle(), tag.getSpotType(), pos.latitude, pos.longitude, null));
                // TODO allow user to pick image when he edits SPOT
            }
        }

        return result;
    }

    private void confirmSaveAndQuit(List<Spot> updatedSpots) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(R.string.button_yes, (dialog1, which) -> {
            repository.updateSpots(((EventShowcaseActivity) requireActivity()).getEventID(), updatedSpots);
            repository.updateZones(((EventShowcaseActivity) requireActivity()).getEventID(), eventZone);
            requireActivity().onBackPressed();
        });
        builder.setNegativeButton(R.string.button_no, (dialog, which) -> {
           // Action is dismissed
        });
        builder.setMessage(R.string.save_and_exit);
        builder.create().show();
    }

    /**
     * Method is here used to do all the onCreate additional work without overriding onCreate()
     * No clustering is wanted in the edition version
     */
    @Override
    protected void setUpCluster() {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            Objects.requireNonNull(getContext()), R.raw.map_edit_style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Set Listeners
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapLongClickListener(this);

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
                this.spotsModel.getSpots().observe(getActivity(), spots -> {
                    if (spots == null) {
                        return;
                    }
                    // Add new spots
                    for (Spot s : spots) {
                        addSpotMarker(s.getTitle(), s.getSnippet(), s.getPosition(), s.getSpotType());
                    }
                });

        }
    }

    /**
     * Adds a spotType marker to the map
     * @param title title of the marker
     * @param snippet snippet of the marker
     * @param position LatLng object representing marker's position
     */
    private Marker addSpotMarker(String title, String snippet, LatLng position, SpotType spotType) {
        Marker m = googleMap.addMarker(new MarkerOptions().title(title).snippet(snippet)
                .draggable(true).position(position));

        m.setTag(createSpotTag(++counterSpot, spotType));
        markerList.add(m);
        positionForMarkerIDMap.put(counterSpot, m.getPosition());

        return m;
    }

    /**
     * Adds all overlay edges to the map at the initialization
     */
    private void overlayEdgeOnMap() {
        if (getActivity() != null){
            this.zonesModel.getZone().observe(getActivity(), zones -> {
                if (zones != null) {
                    eventZone = zones;
                    for (Position p : zones.getPositions()){
                        addOverlayEdgeMarker(p.asLatLng());
                    }
                } else {
                    eventZone = new Zone(new ArrayList<>());
                }
            });
        }
    }

    /**
     * Adds a marker that is an overlay edge on the map
     * @param position LatLng obj as position of the marker
     */
    private Marker addOverlayEdgeMarker(LatLng position) {
        Marker m = googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(hueOverlayBlue))
                .position(position).draggable(true));

        m.setTag(createOverlayEdgeTag(++counterOverlayEdge));
        markerList.add(m);
        positionForMarkerIDMap.put(overlayEdgeIndexShift + counterOverlayEdge, m.getPosition());

        return m;
    }










    @Override
    public boolean onMarkerClick(Marker marker) {
        EventEditionTag tag = (EventEditionTag) marker.getTag();
        if (tag != null && tag.getMarkerType()!=null)
            switch (tag.getMarkerType()) {
                case SPOT:
                    onClickSavedMarkerID = tag.getId();
                    DialogFragment dialogFragment = new CustomMarkerDialog();
                    dialogFragment.setArguments(createInfoBundle(marker.getTitle(), marker.getSnippet(), tag.getSpotType()));
                    dialogFragment.setTargetFragment(this, SPOT_INFO_REQUEST_CODE);
                    showDialogFragment(dialogFragment, MARKER_DIALOG_TAG);
                    return true;

                case OVERLAY_EDGE:
                    // TODO prompt removal of overlay edge

                    return true;
            }
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.i("TAGTEST", String.valueOf(latLng));
        onLongClickSavedLatLng = latLng;
        DialogFragment dialogFragment = new CustomAddOptionsDialog();
        dialogFragment.setTargetFragment(this, ADD_OVERLAY_OR_SPOT_REQUEST_CODE);

        showDialogFragment(dialogFragment, ADD_MARKER_DIALOG_TAG);
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
        EventEditionTag tag = (EventEditionTag) marker.getTag();
        assert tag != null;

        // Index takes a shift depending on type
        int shift = getShift(tag.getMarkerType());
        LatLng ancientPosition = positionForMarkerIDMap.get(shift + tag.getId());
        LatLng newPosition = marker.getPosition();

        history.push(new MoveMarkerAction(
                tag,
                ancientPosition,
                newPosition));

        positionForMarkerIDMap.put(shift + tag.getId(), marker.getPosition());

        if (tag.getMarkerType()==MarkerType.OVERLAY_EDGE)

            // If updating the edge's position did not work
            if (!updatePosition(ancientPosition, newPosition)){
                history.pop();
                Toast.makeText(getContext(), getText(R.string.map_edition_error_not_done),
                        Toast.LENGTH_LONG).show();
            }

            reformPolygon();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The method is called by the CustomAddOptionsDialog i.e. user longClicked
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
        }

        // The method is called by the CustomMarkerDialog i.e. user clicked on Spot marker
        else if (requestCode== SPOT_INFO_REQUEST_CODE) {

            // Updates the marker's info
            String title = data.getStringExtra(CustomMarkerDialog.EXTRA_TITLE);
            String snippet = data.getStringExtra(CustomMarkerDialog.EXTRA_SNIPPET);
            SpotType type = (SpotType) data.getSerializableExtra(CustomMarkerDialog.EXTRA_TYPE);

            Marker m = findMarkerById(onClickSavedMarkerID);

            if (m!=null && (!title.equals(m.getTitle()) || !snippet.equals(m.getSnippet()) ||
                    type != ((EventEditionTag)Objects.requireNonNull(m.getTag())).getSpotType())) {

                history.push(new ModifyMarkerInfoAction(
                        (EventEditionTag) m.getTag(),
                        m.getTitle(), title,
                        m.getSnippet(), snippet, issuedByCreation));

                m.setTitle(title);
                m.setSnippet(snippet);
                int id = ((EventEditionTag)Objects.requireNonNull(m.getTag())).getId();
                m.setTag(createSpotTag(id, type));
            }

            issuedByCreation = false; // resetting the indicator
        }
    }









    /**
     * Handles adding a spotType marker event, i.e displays a window to allow user to edit newly
     * created marker and adds correponding history action.
     * @param onLongClickSavedLatLng saved position to create the marker at
     */
    private void addSpotEvent(LatLng onLongClickSavedLatLng) {
        Marker m = addSpotMarker(defaultTitle, defaultSnippet, onLongClickSavedLatLng, defaultType);

        issuedByCreation = true;
        history.push(new MarkerCreationAction((EventEditionTag) m.getTag(), m.getPosition()));

        onClickSavedMarkerID = counterSpot;
        DialogFragment createSpotDialog = new CustomMarkerDialog();
        createSpotDialog.setArguments(createInfoBundle(defaultTitle, defaultSnippet, defaultType));

        createSpotDialog.setTargetFragment(this, SPOT_INFO_REQUEST_CODE);
        showDialogFragment(createSpotDialog, MARKER_DIALOG_TAG);
    }

    /**
     * Adds an OverlayEdge type marker at the specified position
     * @param onLongClickSavedLatLng saved position representing position to create marker at
     */
    private void addOverlayEdgeEvent(LatLng onLongClickSavedLatLng) {
        eventZone.addPosition(new Position(onLongClickSavedLatLng.latitude, onLongClickSavedLatLng.longitude));
        Marker m = addOverlayEdgeMarker(onLongClickSavedLatLng);

        history.push(new MarkerCreationAction((EventEditionTag) m.getTag(), m.getPosition()));

        reformPolygon();
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

    /**
     * Finds SPOT marker with its id among all the placed markers
     * @param onClickSavedMarkerID id of the SPOT marker to find
     * @return SPOT marker with corresponding id or null if none
     */
    private Marker findMarkerById(int onClickSavedMarkerID) {
        for (Marker m : markerList) {
            EventEditionTag tag = (EventEditionTag) m.getTag();
            if (tag != null &&
                    tag.getMarkerType() == MarkerType.SPOT &&
                    tag.getId() == onClickSavedMarkerID)
                return m;
        }
        return null;
    }

    /**
     * Creates a Bundle containing a title, a snippet text and a MarkerType.
     * @param title String of the title of the marker
     * @param snippet String of the snippet text of the marker
     * @param spotType type of the marker
     * @return Bundle containing marker's attributes
     */
    private Bundle createInfoBundle(String title, String snippet, SpotType spotType) {
        Bundle b = new Bundle();
        b.putString(CustomMarkerDialog.EXTRA_TITLE, title);
        b.putString(CustomMarkerDialog.EXTRA_SNIPPET, snippet);
        b.putSerializable(CustomMarkerDialog.EXTRA_TYPE, spotType);
        return b;
    }

    /**
     * Methotd that computes shift for index in marker's position tracker depending on marker's type.
     * @param type type of the marker to get shift for
     * @return shift acoording to marker's type
     */
    public static int getShift(MarkerType type) {
        return type == MarkerType.SPOT ? 0 : overlayEdgeIndexShift;
    }

    /**
     * Reverts the latest action in history if not empty. It handles linked actions in history and
     * reforming of the polygon
     */
    private void revertLatestAction() {
        MapEditionAction action = history.pop();

        // If the action has a linked action to undo as well
        if (action instanceof ModifyMarkerInfoAction && ((ModifyMarkerInfoAction) action).wasIssudByCreation()) {
            MapEditionAction linkedAction = history.pop();

            if (linkedAction instanceof MarkerCreationAction)
                checkAndRevert(linkedAction);
            else
                history.push(linkedAction);
        }

        // TODO move specific action in specific action's revert
        // If it is a creation or move action for an overlay edge, we need to redraw the overlay
        else if (needToReformOverlayFor(action)){
            if (action instanceof MoveMarkerAction) {
                updatePosition(((MoveMarkerAction) action).getPos(), ((MoveMarkerAction) action).getAncientPos());
                reformPolygon();
                // TODO move this in MoveMarkerAction's revert
            }

            if (action instanceof MarkerCreationAction) {
                LatLng positionOfCreation = ((MarkerCreationAction) action).getPositionOfCreation();

                if(!removeOverlay(positionOfCreation)) return; // stops action from being reverted
            }
        }

        checkAndRevert(action);

    }

    /**
     * Checks if the overlay needs to be reformed i.e. if it was an action on an OVERLAY_EDGE and if it
     * xas a creation or move action.
     * @param action action to be verified
     * @return true if the overlay will need to be reformed
     */
    private boolean needToReformOverlayFor(MapEditionAction action) {
        return (action instanceof MarkerCreationAction || action instanceof MoveMarkerAction)
                && action.getMarkerType()==MarkerType.OVERLAY_EDGE;
    }

    /**
     * Orders action to revert and notifies user of result
     * @param action action to be reverted
     */
    private void checkAndRevert(MapEditionAction action) {
        if (!action.revert(markerList, positionForMarkerIDMap)) {
            Toast.makeText(getContext(), getString(R.string.map_edition_error_undo), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), getString(R.string.undone), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Reforms the overlay that represents on the event
     */
    private void reformPolygon() {
        if (eventOverlay != null)
            eventOverlay.remove();
        eventOverlay = googleMap.addPolygon(eventZone.addPolygon());

    }

    /**
     * Update position of an edge from the overlay.
     * @param ancientPosition ancient edge's position
     * @param newPosition new edge's position
     * @return true if the position wan successfully changed
     */
    private boolean updatePosition(LatLng ancientPosition, LatLng newPosition) {
        return eventZone.changePositionOfElement(
                new Position(ancientPosition.latitude, ancientPosition.longitude),
                new Position(newPosition.latitude, newPosition.longitude));
    }

    /**
     * Removes an overlay edge, reforms overlay on success, makes toast on failure.
     * @param positionOfCreation position of the edge to remove
     * @return true if the edge was removed and the overlay was reformed, false otherwise.
     */
    private boolean removeOverlay(LatLng positionOfCreation) {
        // If the removal was successful
        if (eventZone.removePosition(new Position(positionOfCreation.latitude, positionOfCreation.longitude))) {
            reformPolygon();
            return true;
        }
        else {
            // TODO transform every toast in snackbar because they are displayed for too long
            Toast.makeText(getContext(), getText(R.string.map_edition_error_not_done), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}

