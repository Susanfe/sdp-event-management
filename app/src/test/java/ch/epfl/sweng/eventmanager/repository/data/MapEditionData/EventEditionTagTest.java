package ch.epfl.sweng.eventmanager.repository.data.MapEditionData;

import org.junit.Test;

import ch.epfl.sweng.eventmanager.repository.data.SpotType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class EventEditionTagTest {

    @Test
    public void createSpotTag(){
        EventEditionTag spotTag = EventEditionTag.createSpotTag(0, SpotType.ATM);
        EventEditionTag spotTag1 = EventEditionTag.createSpotTag(1, SpotType.BAR);
        EventEditionTag spotTag2 = EventEditionTag.createSpotTag(0, SpotType.ATM);

        assertThat(spotTag.equals(spotTag2), is(true));
        assertThat(spotTag1.getId(), is(1));
        assertThat(spotTag1.getSpotType(), is(SpotType.BAR));
        assertThat(spotTag1.getMarkerType(), is(MarkerType.SPOT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullSpot() {
        EventEditionTag.createSpotTag(15, null);
    }

    @Test
    public void createOverlayEdgeTag() {
        EventEditionTag edge = EventEditionTag.createOverlayEdgeTag(0);
        EventEditionTag edge1 = EventEditionTag.createOverlayEdgeTag(0);

        assertThat(edge.equals(edge1), is(true));
        assertThat(edge.getId(), is(0));
        assertThat(edge.getMarkerType(), is(MarkerType.OVERLAY_EDGE));
        assertNull(edge.getSpotType());
    }

    @Test
    public void assertDifferent() {
        EventEditionTag spotTag = EventEditionTag.createSpotTag(0, SpotType.ATM);
        EventEditionTag spotTag1 = EventEditionTag.createSpotTag(1, SpotType.BAR);

        assertThat(spotTag.equals(spotTag1), is(false));
    }







}