package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.widget.DatePicker;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.users.Session;
import org.hamcrest.Matchers;
import org.junit.*;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EventCreateActivityTest {
    @Rule
    public final EventTestRule<EventCreateActivity> mActivityRule =
            new EventTestRule<>(EventCreateActivity.class, -1); // Creation


    @Inject
    protected MockEventsRepository repository;

    public EventCreateActivityTest() {
        TestApplication.component.inject(this);
    }

    @Before
    public void setup() {
        Session.enforceDummySessions();
        Session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD, null, null);

        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Test
    public void testCreateEvent() {
        onView(withId(R.id.create_form_name)).perform(typeText("Event Test 1"), closeSoftKeyboard());
        onView(withId(R.id.create_form_email)).perform(typeText("you@test.com"), closeSoftKeyboard());
        this.setDate(R.id.create_form_begin_date,2018,12,1);
        this.setDate(R.id.create_form_end_date,2018,12,4);
        onView(withId(R.id.create_form_description)).perform(typeText("Event Test 1 description"), closeSoftKeyboard());
        onView(withId(R.id.create_form_send_button)).perform(click());

        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(EventAdministrationActivity.class.getName()),
                IntentMatchers.hasExtraWithKey(EventPickingActivity.SELECTED_EVENT_ID))
        );

        // Find event
        boolean found = false;
        for (Event e : repository.getEvents().getValue()) {
            if (e.getName().equalsIgnoreCase("Event Test 1")) {
                found = true;

                Assert.assertEquals("you@test.com", e.getOrganizerEmail());
                Assert.assertEquals("Event Test 1 description", e.getDescription());
                Date beginDate = java.sql.Date.valueOf(LocalDate.of(2018, 12, 1).toString());
                Date endDate = java.sql.Date.valueOf(LocalDate.of(2018, 12, 4).toString());
                Assert.assertEquals(beginDate.getTime(), e.getBeginDate());
                Assert.assertEquals(endDate.getTime(), e.getEndDate());
                Assert.assertNull(e.getTwitterName());
            }
        }

        Assert.assertTrue("Event was not found in repository after creation", found);
    }

    private void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {
        onView(withId(datePickerLaunchViewId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }
}