package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class EventTest {
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

    private final String orgaEmail1 = "fancyemail1@epfl.ch";
    private final String orgaEmail2 = "fancyemail2@google.com";

    private final Event ev1 = new Event(1, "Event1", "Event Description 1",
            start, end,  orgaEmail1, null, null, null, null,null,true);
    private final Event ev2 = new Event(2, "Event2", "Event Description 2",
            start, end, orgaEmail2, null, l1, null, null, "faceJapan",false);


    @Test
    public void getIdTest() {
        assertEquals(2, ev2.getId());

        ev1.setId(200);
        assertEquals(200, ev1.getId());
        ev1.setId(1);
        assertEquals(1, ev1.getId());
    }

    @Test
    public void getNameTest() {
        ev1.setName("Event100");
        assertEquals("Event100", ev1.getName());
        ev1.setName("Event1");
        assertEquals("Event1", ev1.getName());


        assertEquals("Event2", ev2.getName());
    }

    @Test
    public void getDescriptionTest() {
        ev1.setDescription("Event Description 100");
        assertEquals("Event Description 100", ev1.getDescription());
        ev1.setDescription("Event Description 1");
        assertEquals("Event Description 1", ev1.getDescription());

        assertEquals("Event Description 2", ev2.getDescription());
    }

    @Test
    public void getDateTest(){
        assertEquals(ev1.getBeginDateAsDate(), start);
        assertEquals(ev1.getEndDateAsDate(), end);

        ev2.setBeginDate(end.getTime());
        assertEquals(ev2.getBeginDateAsDate(), end);
        ev2.setBeginDate(start.getTime());
        assertEquals(ev2.getBeginDateAsDate(), start);


        ev2.setEndDate(start.getTime());
        assertEquals(ev2.getEndDateAsDate(), start);
        ev2.setEndDate(end.getTime());
        assertEquals(ev2.getEndDateAsDate(), end);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getWrongDateTest(){
        new Event(1, "Event1", "Event Description 1", end, start, orgaEmail1,
                null, null, null, null, null,false);
    }

    @Test
    public void getOrganizerTest() {
        ev1.setOrganizerEmail("random email");
        assertEquals("random email", ev1.getOrganizerEmail());
        ev1.setOrganizerEmail(orgaEmail1);
        assertEquals(orgaEmail1, ev1.getOrganizerEmail());


        assertEquals(orgaEmail2, ev2.getOrganizerEmail());
    }

    @Test
    public void getLocationTest() {
        assertNull(ev1.getLocation());
        EventLocation fake1 = new EventLocation(l1.getName(), l1.getPosition());
        assertEquals(fake1, ev2.getLocation());

        ev1.setLocation(fake1);
        assertEquals(fake1, ev1.getLocation());
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
        String url = "url";
        ev1.setImageURL(url);
        assertEquals(ev1.getImageURL(), url);
    }

    @Test
    public void getFacebookName() {
        assertEquals("faceJapan", ev2.getFacebookName());
    }

    @Test
    public void setAndGetVisibility() {
        ev1.setVisibleFromPublic(false);
        ev2.setVisibleFromPublic(true);
        assertFalse(ev1.isVisibleFromPublic());
        assertTrue(ev2.isVisibleFromPublic());

    }
}