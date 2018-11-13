package ch.epfl.sweng.eventmanager.repository.data;

import android.graphics.Bitmap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class EventTest {
    private List<Spot> spotList = new ArrayList<>();
    private EventLocation l1 = new EventLocation("Fake1", new Position(10, 10));
    private final Date start = new Calendar.Builder()
            .setFields(Calendar.YEAR, 2018,
                    Calendar.MONTH, Calendar.DECEMBER,
                    Calendar.DAY_OF_MONTH, 25,
                    Calendar.HOUR_OF_DAY, 12,
                    Calendar.MINUTE, 0,
                    Calendar.SECOND, 0).build().getTime();
    private final Date end = new Calendar.Builder()
            .setFields(Calendar.YEAR, 2018,
                    Calendar.MONTH, Calendar.DECEMBER,
                    Calendar.DAY_OF_MONTH, 26,
                    Calendar.HOUR_OF_DAY, 12,
                    Calendar.MINUTE, 0,
                    Calendar.SECOND, 0).build().getTime();
    private final EventOrganizer orga1 =
            new EventOrganizer(1, "Orga1", "Organizer 1", null, null);
    private final EventOrganizer orga2 =
            new EventOrganizer(2, "Orga2", "Organizer 2", null, null);
    private final Event ev1 = new Event(1, "Event1", "Event Description 1",
            start, end,  orga1, null, null, spotList, null, null);
    private final Event ev2 = new Event(2, "Event2", "Event Description 2",
            start, end, orga2, null, l1, spotList, null, null);

    @Test
    public void getIdTest() {
        assertEquals(1, ev1.getId());
        assertEquals(2, ev2.getId());
    }

    @Test
    public void getNameTest() {
        assertEquals("Event1", ev1.getName());
        assertEquals("Event2", ev2.getName());
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Event Description 1", ev1.getDescription());
        assertEquals("Event Description 2", ev2.getDescription());
    }

    @Test
    public void getDateTest(){
        assertEquals(ev1.getBeginDate(), start);
        assertEquals(ev1.getEndDate(), end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWrongDateTest(){
        final Event falseDate = new Event(1, "Event1", "Event Description 1", end, start,  orga1,
                null, null, spotList, null, null);
    }

    @Test
    public void getOrganizerTest() {
        assertEquals(orga1, ev1.getOrganizer());
        assertEquals(orga2, ev2.getOrganizer());
    }

    @Test
    public void getSpotsTest() {
        assertTrue(ev1.getSpotList().isEmpty());
        ev1.getSpotList().add(new Spot("Spot 1", SpotType.ROOM, 10, 10));
        // FIXME Encapsulation issue to do so ?
        assertEquals(1, ev1.getSpotList().size());

        spotList.add(new Spot("PMU", SpotType.BAR, 10, 10));
        Event fake1 = new Event(-1, "Fake1", null, new Date(0), new Date(0),
                null, null, null, spotList, null, null);
        assertEquals(1, fake1.getSpotList().size());
    }

    @Test
    public void getLocationTest() {
        assertNull(ev1.getLocation());
        EventLocation fake1 = new EventLocation(l1.getName(), l1.getPosition());
        assertEquals(fake1, ev2.getLocation());
    }

    @Test
    public void dateAsString() {
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        String start_s = f.format(start);
        String end_s = f.format(end);
        assertEquals(start_s, ev1.beginDateAsString());
        assertEquals(end_s, ev1.endDateAsString());
    }

    @Test
    public void setAndGetImageTest(){
        Bitmap img = Mockito.mock(Bitmap.class);
        ev1.setImage(img);
        assertEquals(ev1.getImage(), img);
    }
}