package ch.epfl.sweng.eventmanager.repository.data;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ZoneTest {
    private List<Position> positions = new ArrayList<>();
    private Zone zone1;

    @Before
    public void setUp() {
        positions.add(new Position(0,0));
        positions.add(new Position(2,2));
        zone1 = new Zone(positions);
    }

    @Test
    public void getPositionsWork() {
        assertEquals(0, zone1.getPositions().get(0).getLatitude(), 0.0);
        assertEquals(0, zone1.getPositions().get(0).getLongitude(), 0.0);
    }

    @Test
    public void getAddPolygonWork() {
        List<LatLng> latLngs = new ArrayList<>();
        for(Position p: positions) {
            latLngs.add(p.asLatLng());
        }
        assertEquals(zone1.addPolygon().getPoints(), latLngs);
    }

    @Test
    public void addPositionsWorks() {
        assertThat(zone1.getPositions().size(), is(2));

        // Inserting 2 more positions to make a proper zone
        zone1.addPosition(new Position(0, 2));
        zone1.addPosition(new Position(2, 0));

        assertThat(zone1.getPositions().size(), is(4));

        // Insert positions in order where they should have been inserted in zone
        positions.add(1, new Position(0, 2));
        positions.add(3, new Position(2, 0));
        assertThat(zone1.getPositions(), is(positions));

        //insert a random position in zone
        zone1.addPosition(new Position(1, 4));

        //Add position in correct position in the result list
        positions.add(2, new Position(1, 4));
        assertThat(zone1.getPositions(), is(positions));
    }

    @Test
    public void removePositionWorks() {
        assertThat(zone1.getPositions().size(), is(2));

        zone1.removePosition(new Position(0, 0));
        assertThat(zone1.getPositions(), is(Collections.singletonList(new Position(2, 2))));

        zone1.removePosition(new Position(2, 2));
        assertThat(zone1.getPositions().isEmpty(), is(true));

        boolean removal = zone1.removePosition(new Position(10, 10));
        assertThat(removal, is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullPositionNotWorking() {
        zone1.addPosition(null);
    }

    @Test
    public void changePositionIsCorrect() {
        // Inserting 2 more positions to make a proper zone
        zone1.addPosition(new Position(0, 2));
        zone1.addPosition(new Position(2, 0));

        positions.add(2, new Position(2, 0));

        zone1.changePositionOfElement(new Position(0, 2),
                new Position(3, 1));
        positions.add(2, new Position(3, 1));

        assertThat(zone1.getPositions(), is(positions));

    }

    @Test(expected = IllegalArgumentException.class)
    public void changePositionOfNullFirst(){
        zone1.changePositionOfElement(null, new Position(0, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void changePositionOfNullSecond(){
        zone1.changePositionOfElement(new Position(0, 1), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changePositionOfNullBoth(){
        zone1.changePositionOfElement(null, null);
    }



}
