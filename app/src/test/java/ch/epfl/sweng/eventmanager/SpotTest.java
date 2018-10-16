package ch.epfl.sweng.eventmanager;


import org.junit.Test;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpotTest {

    private Spot spot1 = new Spot("satelitte", SpotType.BAR, 46.520433, 6.567822);


    @Test
    public void getTitleWork() {
        assertEquals(spot1.getTitle(), "satelitte");
    }

    @Test
    public void getSnippetWork() {
        assertEquals(spot1.getSnippet(), "bar");
    }

    @Test
    public void getPositionWork() {
        assertTrue(spot1.getPosition().latitude == 46.520433 && spot1.getPosition().longitude == 6.567822);
    }

}
