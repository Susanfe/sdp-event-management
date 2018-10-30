package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class EventTest {
    private List<Spot> spotList = new ArrayList<>();
    private EventLocation l1 = new EventLocation("Fake1", new Position(10, 10));
    private final EventOrganizer orga1 = new EventOrganizer(1, "Orga1", "Organizer 1", null, null);
    private final EventOrganizer orga2 = new EventOrganizer(2, "Orga2", "Organizer 2", null, null);
    private final Event ev1 = new Event(1, "Event1", "Event Description 1", orga1, null, null,spotList, null);
    private final Event ev2 = new Event(2, "Event2", "Event Description 2", orga2, null, l1,spotList, null);

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
        Event fake1 = new Event(-1, "Fake1", null, null, null, null, spotList, null);
        assertEquals(1, fake1.getSpotList().size());
    }

    @Test
    public void getLocationTest() {
        assertNull(ev1.getLocation());
        EventLocation fake1 = new EventLocation(l1.getName(), l1.getPosition());
        assertEquals(fake1, ev2.getLocation());
    }
}