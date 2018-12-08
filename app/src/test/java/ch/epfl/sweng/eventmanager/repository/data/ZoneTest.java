package ch.epfl.sweng.eventmanager.repository.data;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.R;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
        Context context = mock(Context.class);
        when(context.getResources().getColor(R.color.overlay_blue)).thenReturn(Color.argb(33, 26, 149, 244));
        assertEquals(zone1.addPolygon(context).getPoints(), latLngs);
    }

}
