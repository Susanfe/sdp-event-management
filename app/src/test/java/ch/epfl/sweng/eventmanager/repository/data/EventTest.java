package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class EventTest {
    List<Spot> spotList = new ArrayList<>();
    private final EventOrganizer orga1 = new EventOrganizer(1, "Orga1", "Organizer 1", null, null);
    private final EventOrganizer orga2 = new EventOrganizer(2, "Orga2", "Organizer 2", null, null);
    private final Event ev1 = new Event(1, "Event1", "Event Description 1", orga1, null, null,spotList, null);
    private final Event ev2 = new Event(2, "Event2", "Event Description 2", orga2, null, null,spotList, null);

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
}