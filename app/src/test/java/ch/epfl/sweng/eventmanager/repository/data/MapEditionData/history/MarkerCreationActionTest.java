package ch.epfl.sweng.eventmanager.repository.data.MapEditionData.history;

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

public class MarkerCreationActionTest {
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
    public void createAction() {
        MarkerCreationAction creationAction = new MarkerCreationAction(tag, pos);
        assertThat(creationAction.getMarkerType(), is(MarkerType.SPOT));

        MarkerCreationAction creationAction2 = new MarkerCreationAction(tag, pos);
        assertThat(creationAction.equals(creationAction2), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullActionFirst() {
        new MarkerCreationAction(null, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullActionSecond() {
        new MarkerCreationAction(tag, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullActionBoth() {
        new MarkerCreationAction(null, null);
    }

    @Test
    public void getCreationPosition() {
        MarkerCreationAction creationAction = new MarkerCreationAction(tag, pos);
        assertThat(creationAction.getPositionOfCreation(), is(pos));
    }

    @Test
    public void revertWorks() {
        // Testing with markers is impossible because Marker cannot be directly instanciated
        // Plus mocking is not allowed by mockito because the class is final
        MarkerCreationAction creationAction = new MarkerCreationAction(tag1, pos1);
        assertThat(creationAction.getPositionOfCreation(), is(pos1));

        boolean result = creationAction.revert(markers, null);
        assertThat(markers.size(), is(0));
        assertThat(result, is(false));
    }



}