package ch.epfl.sweng.eventmanager.repository.data;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.SpotType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpotTest {
    private Spot spot = new Spot("CE", SpotType.BAR, 46.520433, 6.567822, null);
    private Spot spot1 = new Spot("satelitte", SpotType.BAR, 46.520433, 6.567822, null);
    private Spot spot2 = new Spot("satelitte", SpotType.INFORMATION, 46.520433, 6.567822, null);
    private Spot spot3 = new Spot("satelitte", SpotType.WC, 46.520433, 6.567822, null);
    private Spot spot4 = new Spot("satelitte", SpotType.NURSERY, 46.520433, 6.567822, null);
    private Spot spot5 = new Spot("satelitte", SpotType.ATM, 46.520433, 6.567822, null);
    private Spot spot6 = new Spot("satelitte", SpotType.ROOM, 46.520433, 6.567822, null);
    private Spot spot7 = new Spot("satelitte", SpotType.SCENE, 46.520433, 6.567822, null);
    private Spot spot8 = new Spot("satelitte", SpotType.STAND, 46.520433, 6.567822, null);


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

    @Test
    public void getSpotTypeWork() {
        assertEquals(spot1.getSpotType(), SpotType.BAR);
    }

    @Test
    public void setScheduleListWork() {
        List<ScheduledItem> items;
        String jsonSchedule = "[ {\n" +
                "  \"artist\" : \"David Guetta\",\n" +
                "  \"date\" : 1744913000000,\n" +
                "  \"description\" : \"Incredible stage performance by famous DJ David Guetta!\",\n" +
                "  \"duration\" : 1.5,\n" +
                "  \"genre\" : \"Electro/Dance\",\n" +
                "  \"id\" : \"b52a251a-6ee5-4ef2-af5b-4475a8244aaf\",\n" +
                "  \"itemLocation\" : \"Polyv\"\n" +
                "}, {\n" +
                "  \"artist\" : \"ABBA\",\n" +
                "  \"date\" : 1744904900000,\n" +
                "  \"description\" : \"Wow! This is the great comeback of the well-known success group!\",\n" +
                "  \"duration\" : 2,\n" +
                "  \"genre\" : \"Rock\",\n" +
                "  \"id\" : \"d41e8f29-51db-4924-8363-1bcc1e98631d\",\n" +
                "  \"itemLocation\" : \"CE\"\n" +
                "}, {\n" +
                "  \"artist\" : \"Daft Punk\",\n" +
                "  \"date\" : 1744904900000,\n" +
                "  \"description\" : \"Le retour des frenchies !\",\n" +
                "  \"duration\" : 1.5,\n" +
                "  \"genre\" : \"Pop/House\",\n" +
                "  \"id\" : \"90b8b958-9915-4647-809d-470122c28265\",\n" +
                "  \"itemLocation\" : \"Polyv\"\n" +
                "}, {\n" +
                "  \"artist\" : \"Black M\",\n" +
                "  \"date\" : 1741980110000,\n" +
                "  \"description\" : \"Le retour !\",\n" +
                "  \"genre\" : \"R&B/Pop\",\n" +
                "  \"id\" : \"7a9207df-202b-4e83-93a5-8466563466ca\",\n" +
                "  \"itemLocation\" : \"CO\"\n" +
                "} ]\n";



        TypeToken<List<ScheduledItem>> scheduleToken = new TypeToken<List<ScheduledItem>>() {
        };
        items = new Gson().fromJson(jsonSchedule, scheduleToken.getType());
        spot.setScheduleList(items);
        assertEquals(1, spot.getScheduleList().size());
        assertEquals(items.get(1).getItemLocation(), spot.getScheduleList().get(0).getItemLocation());
    }

}
