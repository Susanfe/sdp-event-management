package ch.epfl.sweng.eventmanager.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.Converters;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedScheduleItemDao;

public class JoinedScheduleItemTestUtils extends AbstractTestUtils<JoinedScheduleItemDao, JoinedScheduleItem> {

    public final String RANDOM_FIXED_UUID_TAG = "123e4567-e89b-12d3-a456-55664244000";

    @Override
    JoinedScheduleItemDao getDao() {
        return mDb.getJoinedScheduleItemDao();
    }

    /**
     * Create {@param numItems} JoinedScheduleItems with {uuid: i, eventId: i}
     * @param numItems number of events to create
     * @return a list containing created JoinedScheduleItems
     */
    @Override
    protected List<JoinedScheduleItem> createItems(int numItems) {

        List<JoinedScheduleItem> events = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            events.add(new JoinedScheduleItem(RANDOM_FIXED_UUID_TAG + i, i));
        }

        return Collections.unmodifiableList(events);
    }

    @Override
    protected List<JoinedScheduleItem> insertItems(int numItems) {
        List<JoinedScheduleItem> joinedScheduleItems = createItems(numItems);

        for(JoinedScheduleItem joinedScheduleItem: joinedScheduleItems)
            dao.insert(joinedScheduleItem);

        return joinedScheduleItems;
    }
}
