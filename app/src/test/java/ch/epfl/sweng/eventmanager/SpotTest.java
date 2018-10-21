package ch.epfl.sweng.eventmanager;


import org.junit.Test;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpotTest {

    private Spot spot1 = new Spot("satelitte", SpotType.BAR, 46.520433, 6.567822);
    private Spot spot2 = new Spot("satelitte", SpotType.INFORMATION, 46.520433, 6.567822);
    private Spot spot3 = new Spot("satelitte", SpotType.WC, 46.520433, 6.567822);
    private Spot spot4 = new Spot("satelitte", SpotType.NURSERY, 46.520433, 6.567822);
    private Spot spot5 = new Spot("satelitte", SpotType.ATM, 46.520433, 6.567822);
    private Spot spot6 = new Spot("satelitte", SpotType.ROOM, 46.520433, 6.567822);
    private Spot spot7 = new Spot("satelitte", SpotType.SCENE, 46.520433, 6.567822);
    private Spot spot8 = new Spot("satelitte", SpotType.STAND, 46.520433, 6.567822);


    @Test
    public void getTitleBarWork() {
        assertEquals(spot1.getTitle(), "satelitte");
    }

    @Test
    public void getSnippetInfoWork() {
        assertEquals(spot2.getSnippet(), "information");
    }

    @Test
    public void getSnippetWCWork() {
        assertEquals(spot3.getSnippet(), "toilet");
    }

    @Test
    public void getSnippetNurseryWork() {
        assertEquals(spot4.getSnippet(), "nursery");
    }

    @Test
    public void getSnippetATMWork() {
        assertEquals(spot5.getSnippet(), "atm");
    }

    @Test
    public void getSnippetRoomWork() {
        assertEquals(spot6.getSnippet(), "room");
    }

    @Test
    public void getSnippetSceneWork() {
        assertEquals(spot7.getSnippet(), "scene");
    }

    @Test
    public void getSnippetStandWork() {
        assertEquals(spot8.getSnippet(), "stand");
    }

    @Test
    public void getPositionWork() {
        assertTrue(spot1.getPosition().latitude == 46.520433 && spot1.getPosition().longitude == 6.567822);
    }

}
