package ch.epfl.sweng.eventmanager.repository.room;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.eventmanager.repository.JoinedEventTestUtils;
import ch.epfl.sweng.eventmanager.repository.LiveDataTestUtil;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.stream.Collectors;

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
        List<JoinedEvent> joinedEvents = insertEvents(numEvents);

        //Testing for findById
        for (int i = 0; i < numEvents; i++) {
            JoinedEvent ev = LiveDataTestUtil.getValue(mJoinedEventDao.findById(i + 1));
            assertEquals(ev, joinedEvents.get(i));
        }

        //Testing for findByName
        for (int i = 0; i < numEvents; i++) {
            JoinedEvent ev = LiveDataTestUtil.getValue(mJoinedEventDao.findByName("Event#" + (i + 1)));
            assertEquals(ev, joinedEvents.get(i));
        }

        //Testing for null
        assertNull(LiveDataTestUtil.getValue(mJoinedEventDao.findById(100)));
        assertNull(LiveDataTestUtil.getValue(mJoinedEventDao.findByName(null)));


        //Testing for getAll
        List<JoinedEvent> events = LiveDataTestUtil.getValue(mJoinedEventDao.getAll());
        assertEquals(joinedEvents, events);

        //Testing for getAllIds
        List<Integer> ids = LiveDataTestUtil.getValue(mJoinedEventDao.getAllIds());
        assertEquals(joinedEvents.stream().map(JoinedEvent::getUid).collect(Collectors.toList()), ids);

        //Testing for loadAllByIds
        int[] lookFor = {1, 3, 5, 7, 9, 11};
        Set<Integer> lookForSet = Arrays.stream(lookFor).boxed().collect(Collectors.toSet());
        List<JoinedEvent> someEvents = LiveDataTestUtil.getValue(mJoinedEventDao.loadAllByIds(lookFor));
        List<JoinedEvent> expectedEvents = joinedEvents.stream().filter(ev -> lookForSet.contains(ev.getUid())).collect(Collectors.toList());
        assertEquals(expectedEvents, someEvents);
    }

    /**
     * Tests if we can delete data from the database
     */
    @Test
    public void deleteEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        insertEvents(numEvents);

        mJoinedEventDao.delete(new JoinedEvent(3, "Event#3"));

        //Testing for findById
        JoinedEvent nullEvent = LiveDataTestUtil.getValue(mJoinedEventDao.findById(3));
        JoinedEvent notNullEvent = LiveDataTestUtil.getValue(mJoinedEventDao.findById(4));

        assertNull(nullEvent);
        assertNotNull(notNullEvent);

        //Testing for findByName
        JoinedEvent nullEventBis = LiveDataTestUtil.getValue(mJoinedEventDao.findByName("Event#3"));
        JoinedEvent notNullEventBis = LiveDataTestUtil.getValue(mJoinedEventDao.findByName("Event#4"));

        assertNull(nullEventBis);
        assertNotNull(notNullEventBis);

    }

    @Test
    public void updateEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        List<JoinedEvent> joinedEvents = insertEvents(numEvents);

        JoinedEvent newEvent = new JoinedEvent(3, "Event#3-updated");
        mJoinedEventDao.insert(newEvent);

        //Testing for findById
        JoinedEvent updatedEvent = LiveDataTestUtil.getValue(mJoinedEventDao.findById(3));
        assertEquals(updatedEvent, newEvent);

        //Testing for findByName
        JoinedEvent updatedEventBis = LiveDataTestUtil.getValue(mJoinedEventDao.findByName("Event#3-updated"));
        assertEquals(updatedEventBis, newEvent);
    }


}


