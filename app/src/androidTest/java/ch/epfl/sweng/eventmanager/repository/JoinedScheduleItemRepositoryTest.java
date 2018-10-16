package ch.epfl.sweng.eventmanager.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.eventmanager.repository.data.JoinedScheduleItem;
import ch.epfl.sweng.eventmanager.repository.room.Converters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
@RunWith(AndroidJUnit4.class)
public class JoinedScheduleItemRepositoryTest extends JoinedScheduleItemTestUtils {
    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();

    private List<JoinedScheduleItem> scheduleItems;
    private List<UUID> uuids;
    private JoinedScheduleItemRepository repository;

    @Before
    public void setupdb() {
        this.scheduleItems = insertItems(10);
        this.uuids = new ArrayList<>();
        for (JoinedScheduleItem joinedScheduleItem: scheduleItems)
            uuids.add(joinedScheduleItem.getUid());
        this.repository = new JoinedScheduleItemRepository(dao);
    }

    @Test
    public void findAll() throws InterruptedException {
        assertEquals(scheduleItems, getValue(repository.findAll()));
    }

    @Test
    public void findAllIds() throws InterruptedException {
        assertEquals(uuids, getValue(repository.findAllIds()));
    }

    @Test
    public void findById() throws InterruptedException {
        // Should find existing
        for (int i = 0; i < 10; ++i)
            assertEquals(scheduleItems.get(i), getValue(repository.findById(Converters.fromString(RANDOM_FIXED_UUID_TAG + i))));

        // Should not find non existing
        assertNull(getValue(repository.findById(Converters.fromString("223e4567-e89b-12d3-a456-556642440000"))));
    }

    @Test
    public void insert() throws InterruptedException, ExecutionException {

        UUID uuid0 = Converters.fromString(RANDOM_FIXED_UUID_TAG + "0");

        // Should insert
        repository.insert(new JoinedScheduleItem(uuid0, 0));

        // Should be in db after insert
        assertNotNull(getValue(repository.findById(uuid0)));
        assertTrue(getValue(repository.findAllIds()).contains(uuid0));

        boolean test = false;
        for (JoinedScheduleItem it : getValue(repository.findAll()))
            if (it.getUid().equals(uuid0))
                test = true;
        assertTrue(test);
    }

    @Test
    public void delete() throws InterruptedException, ExecutionException {

        UUID uuid1 = Converters.fromString(RANDOM_FIXED_UUID_TAG + "1");

        // Should remove
        repository.delete(new JoinedScheduleItem(uuid1, 1));

        // Should not be in db after delete
     //   assertNull(getValue(repository.findById(uuid1)));
        assertFalse(getValue(repository.findAllIds()).contains(uuid1));

        boolean test = false;
        for (JoinedScheduleItem it : getValue(repository.findAll()))
            if (it.getUid() == uuid1)
                test = true;
        assertFalse(test);
    }

}
*/