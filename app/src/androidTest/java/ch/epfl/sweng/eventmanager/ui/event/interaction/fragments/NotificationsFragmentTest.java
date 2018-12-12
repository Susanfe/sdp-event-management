package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.os.SystemClock;
import android.view.Gravity;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventAdministrationActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class NotificationsFragmentTest {
    @Rule
    public final EventTestRule<EventAdministrationActivity> mActivityRule =
            new EventTestRule<>(EventAdministrationActivity.class);

    @Inject
    Session session;

    private String notificationTitle = "Wonderful Event";
    private String notificationBody = "This is a wonderful notification";

    @Before
    public void setup() {
        TestApplication.component.inject(this);

        session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD, null, null);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_send_notification));
    }

    private void createNotification() {
        SystemClock.sleep(500);

        onView(withId(R.id.notification_title)).perform(typeText(notificationTitle), closeSoftKeyboard());
        onView(withId(R.id.notification_content)).perform(typeText(notificationBody), closeSoftKeyboard());
        onView(withId(R.id.notification_send)).perform(ViewActions.click());
    }

    @Test
    public void testCreateNews() {
        createNotification();

        onView(withText(R.string.send_notification_success)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

}
