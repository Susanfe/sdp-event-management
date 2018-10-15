package ch.epfl.sweng.eventmanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.eventmanager.repository.room.AppDataBase;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Test Unit. Contains all tests linked to the Room Database.
 */
@RunWith(AndroidJUnit4.class)
public class AppDatabaseTests {
    private JoinedEventDao mJoinedEventDao;
    private AppDataBase mDb;

    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).allowMainThreadQueries().build();
        mJoinedEventDao = mDb.getJoinedEventDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    /**
     * Tests if we can retrieve data from the database
     */
    @Test
    public void joinEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        List<JoinedEvent> joinedEvents = TestUtil.createEvents(numEvents);

        for (JoinedEvent joinedEvent : joinedEvents)
            mJoinedEventDao.insert(joinedEvent);

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
    }

    /**
     * Tests if we can delete data from the database
     */
    @Test
    public void deleteEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        List<JoinedEvent> joinedEvents = TestUtil.createEvents(numEvents);

        for (JoinedEvent joinedEvent : joinedEvents)
            mJoinedEventDao.insert(joinedEvent);

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
        List<JoinedEvent> joinedEvents = TestUtil.createEvents(numEvents);

        for (JoinedEvent joinedEvent : joinedEvents)
            mJoinedEventDao.insert(joinedEvent);

        JoinedEvent newEvent = new JoinedEvent(3, "Event#3-updated");
        mJoinedEventDao.insert(newEvent);

        //Testing for findById
        JoinedEvent updatedEvent = LiveDataTestUtil.getValue(mJoinedEventDao.findById(3));
        assertEquals(updatedEvent, newEvent);

        //Testing for findByName
        JoinedEvent updatedEventBis = LiveDataTestUtil.getValue(mJoinedEventDao.findByName("Event#3-updated"));
        assertEquals(updatedEventBis, newEvent);
    }

    private static class TestUtil {
        /**
         * Create a list of joinedEvents with following format JoinedEvent(id, "Event#id)
         *
         * @param numEvents number of events to create
         * @return a list containing all events in numerical order (1 -> numEvents)
         */
        static List<JoinedEvent> createEvents(int numEvents) {

            List<JoinedEvent> events = new ArrayList<>();
            for (int i = 0; i < numEvents; i++) {
                events.add(new JoinedEvent(i + 1, "Event#" + (i + 1)));
            }

            return Collections.unmodifiableList(events);
        }
    }

    /**
     * Defines a way to access LiveData synchronously. Allows to manipulate LiveData instantaneously.
     *
     * @see InstantTaskExecutorRule
     */
    private static class LiveDataTestUtil {
        public static <T> T getValue(LiveData<T> liveData) throws InterruptedException {
            final Object[] data = new Object[1];
            CountDownLatch latch = new CountDownLatch(1);
            Observer<T> observer = new Observer<T>() {
                @Override
                public void onChanged(@Nullable T o) {
                    data[0] = o;
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            };
            liveData.observeForever(observer);
            latch.await(2, TimeUnit.SECONDS);

            return (T) data[0];
        }
    }
}


