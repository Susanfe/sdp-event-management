package ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history;

import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.EventEditionTag;
import ch.epfl.sweng.eventmanager.repository.data.MapEditionData.MarkerType;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MoveMarkerActionTest {

    private EventEditionTag tag;
    private EventEditionTag tag1;
    private LatLng pos;
    private LatLng pos1;
    private List<Marker> markers = new ArrayList<>();

    @Before
    public void setUp() {
        tag = EventEditionTag.createSpotTag(0, SpotType.BAR);
        tag1 = EventEditionTag.createOverlayEdgeTag(0);
        pos1 = new LatLng(1, 1);
        pos = new LatLng(0, 0);
    }

    @Test
    public void createMoveMarkerAction(){
        MoveMarkerAction action = new MoveMarkerAction(tag, pos1, pos);

        assertThat(action.getMarkerType(), is(MarkerType.SPOT));
        assertThat(action.getAncientPos(), is(pos1));
        assertThat(action.getPos(), is(pos));
    }

    @Test
    public void revertWorks() {
        // Testing with markers is impossible because Marker cannot be directly instanciated
        // Plus mocking is not allowed by mockito because the class is final
        MoveMarkerAction creationAction = new MoveMarkerAction(tag1, pos1, pos);

        SparseArray<LatLng> positions = new SparseArray<>();
        boolean result = creationAction.revert(markers, positions);

        assertThat(markers.size(), is(0));
        assertThat(result, is(false));

        assertThat(positions.size(), is(0));
    }

}