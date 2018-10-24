package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class EventOrganizerTest {
    private final EventOrganizer orga1 = new EventOrganizer(1, "Orga1", "Organizer 1", null);
    private final EventOrganizer orga2 = new EventOrganizer(2, "Orga2", "Organizer 2", null);
    @Test
    public void getIdTest() throws Exception {
        assertEquals(1, orga1.getId());
        assertEquals(2, orga2.getId());
    }

    @Test
    public void getNameTest() throws Exception {
        assertEquals("Orga1", orga1.getName());
        assertEquals("Orga2", orga2.getName());
    }

    @Test
    public void getDescriptionTest() throws Exception {
        assertEquals("Organizer 1", orga1.getDescription());
        assertEquals("Organizer 2", orga2.getDescription());
    }
}