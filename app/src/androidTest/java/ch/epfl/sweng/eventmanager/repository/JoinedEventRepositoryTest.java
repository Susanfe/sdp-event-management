package ch.epfl.sweng.eventmanager.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author Louis Vialar
 */
@RunWith(AndroidJUnit4.class)
public class JoinedEventRepositoryTest extends JoinedEventTestUtils {
    @Rule
    public TestRule testRule = new InstantTaskExecutorRule();

    private List<JoinedEvent> events;
    private List<Integer> eventIds;
    private JoinedEventRepository repository;

    @Before
    public void setupdb() {
        this.events = insertItems(10);
        this.eventIds = new ArrayList<>();
        for (JoinedEvent ev : events)
            eventIds.add(ev.getUid());
        this.repository = new JoinedEventRepository(dao);
    }

    @Test
    public void findAll() throws InterruptedException {
        assertEquals(events, getValue(repository.findAll()));
    }

    @Test
    public void findAllIds() throws InterruptedException {
        assertEquals(eventIds, getValue(repository.findAllIds()));
    }

    @Test
    public void findById() throws InterruptedException {
        // Should find existing
        for (int i = 0; i < 10; ++i)
            assertEquals(events.get(i), getValue(repository.findById(i + 1)));

        // Should not find non existing
        assertNull(getValue(repository.findById(100)));
    }

    @Test
    public void findByName() throws InterruptedException {
        // Should find existing
        for (int i = 0; i < 10; ++i)
            assertEquals(events.get(i), getValue(repository.findByName(nameFromId(i + 1))));

        // Should not find non existing
        assertNull(getValue(repository.findByName(null)));
        assertNull(getValue(repository.findByName("not existing")));
    }

    @Test
    public void insert() throws InterruptedException, ExecutionException {
        // Should insert
        repository.insert(new JoinedEvent(50, nameFromId(50)));
        wait(1000L);

        // Should be in db after insert
        assertNotNull("findById didn't find eventId = 50", getValue(repository.findById(50)));
        assertNotNull("findByName didn't find Event#50", getValue(repository.findByName(nameFromId(50))));
        assertTrue("findAllIds didn't contain eventId = 50", getValue(repository.findAllIds()).contains(50));

        boolean test = false;
        for (JoinedEvent ev : getValue(repository.findAll()))
            if (ev.getUid() == 50)
                test = true;
        assertTrue("None of the events matched event uid = 50", test);
    }

    @Test
    public void delete() throws InterruptedException, ExecutionException {
        // Should remove
        repository.delete(new JoinedEvent(1, nameFromId(1)));
        wait(1000L);

        // Should not be in db after delete
        assertNull("findById found deleted event1", getValue(repository.findById(1)));
        assertNull("findByName found deleted Event#1", getValue(repository.findByName(nameFromId(1))));
        assertFalse("findAllIds contains deleted event1", getValue(repository.findAllIds()).contains(1));

        boolean test = false;
        for (JoinedEvent ev : getValue(repository.findAll()))
            if (ev.getUid() == 1)
                test = true;
        assertFalse("findAll found deleted event", test);
    }
}