package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.schedule;

import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventAdministrationActivity;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class ScheduleCreateTest {
    @Rule
    public final EventTestRule<EventAdministrationActivity> mActivityRule =
            new EventTestRule<>(EventAdministrationActivity.class, 2);

    @Inject
    MockEventsRepository repository;

    public ScheduleCreateTest() {
        TestApplication.component.inject(this);
    }

    @Before
    public void navigateToSchedule() {
        repository.getScheduledItems().put(2, new ArrayList<>()); // Set empty list.

        // Open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onIdle();

        // Switch to the 'event edition' section
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_schedule));

        onIdle();

        Intents.init();
    }

    @After
    public void clean() {
        Intents.release();
    }

    @Test
    public void testCreateEmpty() {
        onView(withId(R.id.create_form_send_button)).perform(scrollTo(), click());

        // Nothing should happen
        assertTrue(repository.getScheduledItems(2).getValue().isEmpty());
    }

    @Test
    public void testCreateNormal() {
        onView(withId(R.id.create_form_artist)).perform(typeText("Test Artist"), closeSoftKeyboard());
        onView(withId(R.id.create_form_genre)).perform(typeText("Test Type"), closeSoftKeyboard());
        onView(withId(R.id.create_form_room)).perform(typeText("Test Room"), closeSoftKeyboard());
        onView(withId(R.id.create_form_description)).perform(typeText("Test Description"), closeSoftKeyboard());
        setTime(R.id.create_form_begin_time, 1, 0);
        setTime(R.id.create_form_duration, 2, 0);
        setDate(R.id.create_form_begin_date, 2018, 1, 1);

        onView(withId(R.id.create_form_send_button)).perform(click());

        assertFalse(repository.getScheduledItems(2).getValue().isEmpty());

        ScheduledItem item = repository.getScheduledItems(2).getValue().get(0);

        assertEquals("Test Artist", item.getArtist());
        assertEquals("Test Type", item.getGenre());
        assertEquals("Test Room", item.getItemLocation());
        assertEquals("Test Description", item.getDescription());
        assertEquals(2D, item.getDuration(), 0);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(item.getJavaDate());

        assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.MONTH));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(2018, calendar.get(Calendar.YEAR));
    }

    private void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {
        onView(withId(datePickerLaunchViewId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void setTime(int launchViewId, int hour, int minutes) {
        onView(withId(launchViewId)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hour, minutes));
        onView(withId(android.R.id.button1)).perform(click());
    }

}