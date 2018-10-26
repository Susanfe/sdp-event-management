package ch.epfl.sweng.eventmanager.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Louis Vialar
 */
public class JoinedEventTest {
    @Test
    public void testEquals() {
        String name1 = "Event1";
        String name2 = "Event2";

        JoinedEvent ev1 = new JoinedEvent(1, name1);
        JoinedEvent ev2 = new JoinedEvent(1, name1);

        JoinedEvent ev3 = new JoinedEvent(2, name1);
        JoinedEvent ev4 = new JoinedEvent(1, name2);
        JoinedEvent ev5 = new JoinedEvent(2, name2);

        assertTrue(ev1.equals(ev2));
        assertTrue(ev2.equals(ev2));

        assertFalse(ev1.equals(ev4));
        assertFalse(ev1.equals(ev5));
        assertFalse(ev3.equals(ev4));
        assertFalse(ev3.equals(ev5));
        assertFalse(ev4.equals(ev5));

        assertFalse(ev1.equals(null));
        assertFalse(ev1.equals("other"));
    }

    @Test
    public void testSetters() {
        JoinedEvent ev = new JoinedEvent(1, "Event 1");

        ev.setName("Event 2");
        assertEquals("Event 2", ev.getName());

        ev.setUid(2);
        assertEquals(2, ev.getUid());
    }

    @Test
    public void testCopyConstructor() {
        List<Spot> spotList = new ArrayList<>();
        Event ev = new Event(1, "Event 1", "Descr 1", null, null, null,spotList);
        JoinedEvent copy = new JoinedEvent(ev);

        assertEquals(1, copy.getUid());
        assertEquals("Event 1", copy.getName());

    }
}