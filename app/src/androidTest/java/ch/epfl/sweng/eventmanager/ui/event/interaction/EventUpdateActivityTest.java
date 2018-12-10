package ch.epfl.sweng.eventmanager.ui.event.interaction;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import org.hamcrest.Matchers;
import org.junit.*;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EventUpdateActivityTest {
    @Rule
    public final EventTestRule<EventCreateActivity> mActivityRule =
            new EventTestRule<>(EventCreateActivity.class); // Creation


    @Inject
    protected MockEventsRepository repository;
    @Inject
    Session session;

    public EventUpdateActivityTest() {
        TestApplication.component.inject(this);
    }

    @Before
    public void setup() {
        session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD, null, null);

        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Test
    public void testUpdateEvent() {
        onView(withId(R.id.create_form_name)).perform(clearText(), typeText("Event Test 2"), closeSoftKeyboard());
        onView(withId(R.id.create_form_send_button)).perform(click());

        Intents.intended(Matchers.allOf(
                IntentMatchers.hasComponent(EventAdministrationActivity.class.getName()),
                IntentMatchers.hasExtra(EventPickingActivity.SELECTED_EVENT_ID, 1))
        );

        // Find event

        Assert.assertEquals("Event Test 2", repository.getEvent(1).getValue().getName());
    }
}