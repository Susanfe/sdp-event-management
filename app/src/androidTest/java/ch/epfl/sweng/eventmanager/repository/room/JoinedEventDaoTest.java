package ch.epfl.sweng.eventmanager.repository.room;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.runner.AndroidJUnit4;
import ch.epfl.sweng.eventmanager.repository.JoinedEventTestUtils;
import ch.epfl.sweng.eventmanager.repository.LiveDataTestUtil;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test Unit. Contains all tests linked to the Room Database.
 */

@RunWith(AndroidJUnit4.class)
public class JoinedEventDaoTest extends JoinedEventTestUtils {

    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();
    /**
     * Tests if we can retrieve data from the database
     */
    @Test
    public void joinEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        List<JoinedEvent> joinedEvents = insertItems(numEvents);

        //Testing for findById
        for (int i = 0; i < numEvents; i++) {
            JoinedEvent ev = LiveDataTestUtil.getValue(dao.findById(i + 1));
            assertEquals(ev, joinedEvents.get(i));
        }

        //Testing for findByName
        for (int i = 0; i < numEvents; i++) {
            JoinedEvent ev = LiveDataTestUtil.getValue(dao.findByName("Event#" + (i + 1)));
            assertEquals(ev, joinedEvents.get(i));
        }

        //Testing for null
        assertNull(LiveDataTestUtil.getValue(dao.findById(100)));
        assertNull(LiveDataTestUtil.getValue(dao.findByName(null)));


        //Testing for getAll
        List<JoinedEvent> events = LiveDataTestUtil.getValue(dao.getAll());
        assertEquals(joinedEvents, events);

        //Testing for getAllIds
        List<Integer> ids = LiveDataTestUtil.getValue(dao.getAllIds());
        List<Integer> expectedIds = new ArrayList<>();
        for (JoinedEvent ev : joinedEvents) expectedIds.add(ev.getUid());
        assertEquals(expectedIds, ids);

        //Testing for loadAllByIds
        int[] lookFor = {1, 3, 5, 7, 9, 11};
        Set<Integer> lookForSet = new HashSet<>();
        for (int i : lookFor) lookForSet.add(i);
        List<JoinedEvent> someEvents = LiveDataTestUtil.getValue(dao.loadAllByIds(lookFor));
        List<JoinedEvent> expectedEvents = new ArrayList<>();
        for (JoinedEvent ev : joinedEvents)
            if (lookForSet.contains(ev.getUid()))
                expectedEvents.add(ev);
        assertEquals(expectedEvents, someEvents);
    }

    /**
     * Tests if we can delete data from the database
     */


    @Test
    public void deleteEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        insertItems(numEvents);

        dao.delete(new JoinedEvent(3, "Event#3"));

        //Testing for findById
        JoinedEvent nullEvent = LiveDataTestUtil.getValue(dao.findById(3));
        JoinedEvent notNullEvent = LiveDataTestUtil.getValue(dao.findById(4));

        assertNull(nullEvent);
        assertNotNull(notNullEvent);

        //Testing for findByName
        JoinedEvent nullEventBis = LiveDataTestUtil.getValue(dao.findByName("Event#3"));
        JoinedEvent notNullEventBis = LiveDataTestUtil.getValue(dao.findByName("Event#4"));

        assertNull(nullEventBis);
        assertNotNull(notNullEventBis);

    }

    @Test
    public void updateEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        List<JoinedEvent> joinedEvents = insertItems(numEvents);

        JoinedEvent newEvent = new JoinedEvent(3, "Event#3-updated");
        dao.insert(newEvent);

        //Testing for findById
        JoinedEvent updatedEvent = LiveDataTestUtil.getValue(dao.findById(3));
        assertEquals(updatedEvent, newEvent);

        //Testing for findByName
        JoinedEvent updatedEventBis = LiveDataTestUtil.getValue(dao.findByName("Event#3-updated"));
        assertEquals(updatedEventBis, newEvent);
    }


}


