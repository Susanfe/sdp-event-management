package ch.epfl.sweng.eventmanager.repository;

import ch.epfl.sweng.eventmanager.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Louis Vialar
 */
public abstract class JoinedEventTestUtils extends AbstractTestUtils<JoinedEventDao, JoinedEvent> {

    @Override
    JoinedEventDao getDao(){
        return mDb.getJoinedEventDao();
    }

    /**
     * Create a list of joinedEvents with following format JoinedEvent(id, "Event#id)
     *
     * @param numItems number of events to create
     * @return a list containing all events in numerical order (1 -> numEvents)
     */
    @Override
    protected List<JoinedEvent> createItems(int numItems) {

        List<JoinedEvent> events = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            events.add(new JoinedEvent(i + 1, nameFromId(i + 1)));
        }

        return Collections.unmodifiableList(events);
    }

    protected static String nameFromId(int id) {
        return "Event#" + id;
    }

    @Override
    protected List<JoinedEvent> insertItems(int numItems) {
        List<JoinedEvent> joinedEvents = createItems(numItems);

        for (JoinedEvent joinedEvent : joinedEvents)
            dao.insert(joinedEvent);

        return joinedEvents;
    }

}
