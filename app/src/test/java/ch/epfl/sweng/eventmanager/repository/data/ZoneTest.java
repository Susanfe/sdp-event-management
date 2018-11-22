package ch.epfl.sweng.eventmanager.repository.data;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ZoneTest {
    List<Position> positions = new ArrayList<>();
    private Zone zone1;

    @Before
    public void setUp() {
        positions.add(new Position(0,0));
        positions.add(new Position(2,2));
        zone1 = new Zone(positions);
    }

    @Test
    public void getPositionsWork() {
        assertTrue(zone1.getPositions().get(0).getLatitude() == 0);
        assertTrue(zone1.getPositions().get(0).getLongitude() == 0);
    }

    @Test
    public void getAddPolygonWork() {
        List<LatLng> latLngs = new ArrayList<>();
        for(Position p: positions) {
            latLngs.add(p.asLatLng());
        }
        assertEquals(zone1.addPolygon().getPoints(), latLngs);
    }

}
