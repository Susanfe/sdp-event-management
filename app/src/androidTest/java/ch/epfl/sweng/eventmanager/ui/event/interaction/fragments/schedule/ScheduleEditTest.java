package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.os.SystemClock;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventAdministrationActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.myschedule.MyScheduleTest.withIndex;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScheduleEditTest {
    @Rule
    public final EventTestRule<EventAdministrationActivity> mActivityRule =
            new EventTestRule<>(EventAdministrationActivity.class, 2);

    @Inject
    MockEventsRepository repository;

    public ScheduleEditTest() {
        TestApplication.component.inject(this);
    }

    @Before
    public void navigateToSchedule() {
        repository.getScheduledItems().put(2, new ArrayList<>()); // Set empty list.
        repository.getScheduledItems().getMap().get(2).add(new ScheduledItem(
                new Date(), "Test Artist", "Test Type", "Test Description", 2, "random_id", "EPFL"
        ));

        // Open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onIdle();

        // Switch to the 'event edition' section
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_schedule));

        onIdle();

        onView(withId(R.id.viewpager)).perform(swipeLeft()).check(matches(isCompletelyDisplayed()));
        SystemClock.sleep(500);

        Intents.init();
    }

    @After
    public void clean() {
        Intents.release();
    }

    @Test
    public void testDelete() {
        onView(withId(R.id.text_timeline_description)).perform(longClick());
        onView(withText(R.string.dialog_delete_scheduled_item_confirm)).perform(click());

        assertTrue(repository.getScheduledItems(2).getValue().isEmpty());
    }

    @Test
    public void testEdit() {
        onView(withId(R.id.create_form_artist)).perform(clearText(), typeText("Avicii"), closeSoftKeyboard());
        onView(withId(R.id.create_form_genre)).perform(clearText(), typeText("Electro"), closeSoftKeyboard());
        onView(withId(R.id.create_form_room)).perform(clearText(), typeText("Polyv"), closeSoftKeyboard());
        onView(withId(R.id.create_form_description)).perform(clearText(), typeText("Back from the grave!"), closeSoftKeyboard());

        ScheduledItem item = repository.getScheduledItems(2).getValue().get(0);

        assertEquals("Avicii", item.getArtist());
        assertEquals("Electro", item.getGenre());
        assertEquals("Polyv", item.getItemLocation());
        assertEquals("Back from the grave!", item.getDescription());

    }

}