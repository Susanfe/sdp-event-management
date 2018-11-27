package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.Gravity;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.test.ticketing.MockTicketingServiceManager;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingManager;
import ch.epfl.sweng.eventmanager.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.users.Session;
import org.hamcrest.Matchers;
import org.junit.*;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
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
                Assert.assertNull(e.getTwitterName());
            }
        }

        Assert.assertTrue("Event was not found in repository after creation", found);
    }
}