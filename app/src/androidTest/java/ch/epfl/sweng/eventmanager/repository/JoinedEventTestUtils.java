package ch.epfl.sweng.eventmanager.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.AppDataBase;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
public abstract class JoinedEventTestUtils extends LiveDataTestUtil {
    protected JoinedEventDao mJoinedEventDao;
    protected AppDataBase mDb;

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
     * Create a list of joinedEvents with following format JoinedEvent(id, "Event#id)
     *
     * @param numEvents number of events to create
     * @return a list containing all events in numerical order (1 -> numEvents)
     */
    protected static List<JoinedEvent> createEvents(int numEvents) {

        List<JoinedEvent> events = new ArrayList<>();
        for (int i = 0; i < numEvents; i++) {
            events.add(new JoinedEvent(i + 1, nameFromId(i + 1)));
        }

        return Collections.unmodifiableList(events);
    }

    protected static String nameFromId(int id) {
        return "Event#" + id;
    }

    protected List<JoinedEvent> insertEvents(int numEvents) {
        List<JoinedEvent> joinedEvents = createEvents(numEvents);

        for (JoinedEvent joinedEvent : joinedEvents)
            mJoinedEventDao.insert(joinedEvent);

        return joinedEvents;
    }

}
