package ch.epfl.sweng.eventmanager.repository.room;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import ch.epfl.sweng.eventmanager.repository.JoinedScheduleItemTestUtils;
import ch.epfl.sweng.eventmanager.repository.LiveDataTestUtil;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JoinedScheduleItemDao extends JoinedScheduleItemTestUtils {

    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();
    /**
     * Tests if we can retrieve data from the database
     */
    @Test
    public void joinEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        List<JoinedScheduleItem> joinedScheduleItems = insertItems(numEvents);

        //Testing for findById
        for (int i = 0; i < numEvents; i++) {
            JoinedScheduleItem joinedScheduleItem = LiveDataTestUtil.getValue(dao.findById(RANDOM_FIXED_UUID_TAG + i));
            assertEquals(joinedScheduleItem, joinedScheduleItems.get(i));
        }

        //Testing for null
        assertNull(LiveDataTestUtil.getValue(dao.findById("223e4567-e89b-12d3-a456-556642440000")));


        //Testing for getAll
        List<JoinedScheduleItem> joinedScheduleItems1 = LiveDataTestUtil.getValue(dao.getAll());
        assertEquals(joinedScheduleItems1, joinedScheduleItems);

        //Testing for getAllIds
        List<String> uuids = LiveDataTestUtil.getValue(dao.getAllIds());
        List<String> expectedUuids = new ArrayList<>();
        for (JoinedScheduleItem joinedScheduleItem : joinedScheduleItems) expectedUuids.add(joinedScheduleItem.getUid());
        assertEquals(expectedUuids, uuids);

        //Testing for loadAllByIds
        String[] lookFor = {
                RANDOM_FIXED_UUID_TAG + 1,
                RANDOM_FIXED_UUID_TAG + 3,
                RANDOM_FIXED_UUID_TAG + 5,
                RANDOM_FIXED_UUID_TAG + 7,
                RANDOM_FIXED_UUID_TAG + 9,
                RANDOM_FIXED_UUID_TAG + 11};
        Set<String> lookForSet = new HashSet<>(Arrays.asList(lookFor));
        List<JoinedScheduleItem> someEvents = LiveDataTestUtil.getValue(dao.loadAllByIds(lookFor));
        List<JoinedScheduleItem> expectedScheduleItems = new ArrayList<>();
        for (JoinedScheduleItem it : joinedScheduleItems)
            if (lookForSet.contains(it.getUid()))
                expectedScheduleItems.add(it);
        assertEquals(expectedScheduleItems, someEvents);
    }

    /**
     * Tests if we can delete data from the database
     */
    @Test
    public void deleteEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        insertItems(numEvents);

        dao.delete(new JoinedScheduleItem(RANDOM_FIXED_UUID_TAG + 3, 3));

        //Testing for findById
        JoinedScheduleItem nullEvent = LiveDataTestUtil.getValue(dao.findById(RANDOM_FIXED_UUID_TAG + 3));
        JoinedScheduleItem notNullEvent = LiveDataTestUtil.getValue(dao.findById(RANDOM_FIXED_UUID_TAG + 4));

        assertNull(nullEvent);
        assertNotNull(notNullEvent);
    }

    @Test
    public void updateEventsAndRead() throws InterruptedException {
        int numEvents = 10;
        List<JoinedScheduleItem> joinedScheduleItems1 = insertItems(numEvents);

        JoinedScheduleItem newScheduleItem = new JoinedScheduleItem(RANDOM_FIXED_UUID_TAG + 3, 4);
        dao.insert(newScheduleItem);

        //Testing for findById
        JoinedScheduleItem updatedEvent = LiveDataTestUtil.getValue(dao.findById(RANDOM_FIXED_UUID_TAG + 3));
        assertEquals(updatedEvent, newScheduleItem);

    }
}
