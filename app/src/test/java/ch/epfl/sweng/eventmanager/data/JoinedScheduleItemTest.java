package ch.epfl.sweng.eventmanager.data;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class JoinedScheduleItemTest {
    @Test
    public void testEquals() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        JoinedScheduleItem it1 = new JoinedScheduleItem(uuid1, 1);
        JoinedScheduleItem it2 = new JoinedScheduleItem(uuid1, 1);

        JoinedScheduleItem it3 = new JoinedScheduleItem(uuid2, 1);
        JoinedScheduleItem it4 = new JoinedScheduleItem(uuid1, 2);
        JoinedScheduleItem it5 = new JoinedScheduleItem(uuid2, 2);

        assertTrue(it1.equals(it2));
        assertTrue(it2.equals(it2));

        assertFalse(it1.equals(it4));
        assertFalse(it1.equals(it5));
        assertFalse(it3.equals(it4));
        assertFalse(it3.equals(it5));
        assertFalse(it4.equals(it5));

        assertFalse(it1.equals(null));
        assertFalse(it1.equals("other"));
    }

    @Test
    public void testSetters() {
        JoinedScheduleItem it = new JoinedScheduleItem(UUID.randomUUID(), 1);

        it.setEventId(2);
        assertEquals(2, it.getEventId());

        UUID id = UUID.randomUUID();
        it.setUid(id);
        assertEquals(id, it.getUid());
    }
}