package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
public class JoinedScheduleItemTest {
    @Test
    public void testEquals() {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        JoinedScheduleItem it1 = new JoinedScheduleItem(uuid1, 1);
        JoinedScheduleItem it2 = new JoinedScheduleItem(uuid1, 1);

        JoinedScheduleItem it3 = new JoinedScheduleItem(uuid2, 1);
        JoinedScheduleItem it4 = new JoinedScheduleItem(uuid1, 2);
        JoinedScheduleItem it5 = new JoinedScheduleItem(uuid2, 2);

        assertEquals(it1, it2);
        assertEquals(it2, it2);

        assertNotEquals(it1, it4);
        assertNotEquals(it1, it5);
        assertNotEquals(it3, it4);
        assertNotEquals(it3, it5);
        assertNotEquals(it4, it5);

        assertNotEquals("other", it1);
    }

    @Test
    public void testSetters() {
        JoinedScheduleItem it = new JoinedScheduleItem(UUID.randomUUID().toString(), 1);

        it.setEventId(2);
        assertEquals(2, it.getEventId());

        String id = UUID.randomUUID().toString();
        it.setUid(id);
        assertEquals(id, it.getUid());
    }
}