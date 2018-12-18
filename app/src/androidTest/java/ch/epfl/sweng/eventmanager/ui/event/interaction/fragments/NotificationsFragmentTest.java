package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.content.Context;
import android.view.Gravity;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.notifications.CloudMessageEventStrategy;
import ch.epfl.sweng.eventmanager.notifications.NotificationRequestResponse;
import ch.epfl.sweng.eventmanager.notifications.NotificationScheduler;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventAdministrationActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
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
    private String notificationTitleFails = "fails";
    private String notificationBodyFails = "This is a wonderful notification";

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

    @Test
    public void createNotificationTest() {
        onView(withId(R.id.notification_title)).perform(typeText(notificationTitle), closeSoftKeyboard());
        onView(withId(R.id.notification_content)).perform(typeText(notificationBody), closeSoftKeyboard());
        onView(withId(R.id.notification_send)).perform(ViewActions.click());

        onView(withText(R.string.send_notification_success)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Ignore
    @Test
    public void alreadySubmittedFeedbackTest(){
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_send_news));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_send_notification));


        onView(withId(R.id.notification_title)).perform(typeText(notificationTitleFails), closeSoftKeyboard());
        onView(withId(R.id.notification_content)).perform(typeText(notificationBodyFails), closeSoftKeyboard());
        onView(withId(R.id.notification_send)).perform(ViewActions.click());

        onView(withText(R.string.send_notification_fails)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void cannotSendEmptyBodyNotification(){
        onView(withId(R.id.notification_title)).perform(typeText(notificationTitle), closeSoftKeyboard());

        onView(withId(R.id.notification_send)).perform(click());
        onView(withId(R.id.notification_content)).check(matches(hasErrorText(getResourceString(R.string.send_notification_form_error))));
    }

    @Test
    public void scheduleNotificationDoesntThrowExceptionTest(){
        NotificationScheduler.scheduleNotification(new NotificationRequestResponse("title", "body", "Sysmic"), new CloudMessageEventStrategy(mActivityRule.getActivity().getApplicationContext()));
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
