package ch.epfl.sweng.eventmanager;


import org.junit.Test;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpotTest {

    Spot spot1 = new Spot("satelitte", SpotType.BAR, 46.520433, 6.567822);


    @Test
    public void getNameWork() {
        assertEquals(spot1.getName(), "satelitte");
    }

    @Test
    public void getSpotTypeWork() {
        assertEquals(spot1.getSpotType(), SpotType.BAR);
    }

    @Test
    public void getLatitudeWork() {
        assertTrue(spot1.getLatitude() == 46.520433);
    }

    @Test
    public void getLongitudeWork() {
        assertTrue(spot1.getLongitude() == 6.567822);
    }

}
