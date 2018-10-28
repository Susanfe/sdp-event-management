package ch.epfl.sweng.eventmanager.repository.data;

import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class PositionTest {
    private final double latitude = 1;
    private final double longitude = 2;
    private final Position position = new Position(latitude, longitude);

    @Test
    public void getLatitudeTest() throws Exception {
        assertEquals(latitude, position.getLatitude(), 0D);
    }

    @Test
    public void getLongitudeTest() throws Exception {
        assertEquals(longitude, position.getLongitude(), 0D);
    }

    @Test
    public void asLatLngTest() throws Exception {
        assertEquals(new LatLng(latitude, longitude), position.asLatLng());
    }
}