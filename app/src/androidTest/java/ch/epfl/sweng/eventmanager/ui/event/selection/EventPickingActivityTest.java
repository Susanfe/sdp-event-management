package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.app.Activity;
import android.os.SystemClock;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.users.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Collection;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static ch.epfl.sweng.eventmanager.TestHelper.waitId;

public class EventPickingActivityTest {

    private String email = "lamb.da@domain.tld";
    private String password = "secret";
    private Activity currentActivity;

    @Inject
    Session session;

    @Rule
    public final EventTestRule<EventPickingActivity> mActivityRule = new EventTestRule<>(EventPickingActivity.class);

    @After
    public void autoLogOut() {
        session.logout();
    }

    @Before
    public void setup() {
        TestApplication.component.inject(this);
    }

    @Test
    public void testLoginFeature() {
        ifLoggedInLogOut();

        onView(withId(R.id.event_picking_login_account)).perform(click());
        SystemClock.sleep(1000);

        assertNotLoggedUIstate(ViewMatchers.Visibility.VISIBLE);
        assertLoggedUIstate(ViewMatchers.Visibility.GONE);

        pressBack();
    }

    @Test
    public void testLogoutFeature() {
        session.login(email, password, getActivityInstance(), task -> {});
        pressBack();

        onView(withId(R.id.event_picking_login_account)).perform(click());
        SystemClock.sleep(1000);
        assertLoggedUIstate(ViewMatchers.Visibility.VISIBLE);
        assertNotLoggedUIstate(ViewMatchers.Visibility.GONE);

        pressBack();
    }

    /**
     * Method that tests the UI state {VISIBLE, GONE} of logged version of the login UI
     * @param state {VISIBLE, GONE} state
     */
    private void assertLoggedUIstate(ViewMatchers.Visibility state) {
        onView(withId(R.id.layout_login_signup_logged)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_logout_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_account_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));
    }

    /**
     * Method that tests the UI state {VISIBLE, GONE} of not logged version of the login UI
     * @param state {VISIBLE, GONE} state
     */
    private void assertNotLoggedUIstate(ViewMatchers.Visibility state) {
        onView(withId(R.id.layout_login_signup_not_logged)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_login_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_signup_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));
    }

    @Test
    public void testSignupButton() {
        ifLoggedInLogOut();

        onView(withId(R.id.event_picking_login_account)).perform(click());
        onView(withId(R.id.layout_login_signup_signup_button)).perform(click());

        SystemClock.sleep(1000);

        onView(isRoot()).perform(waitId(R.id.signup_toolbar, 2000));
    }

    @Test
    public void testLoginButton() {
        ifLoggedInLogOut();


        onView(withId(R.id.event_picking_login_account)).perform(click());
        onView(withId(R.id.layout_login_signup_login_button)).perform(click());

        SystemClock.sleep(1000);

        onView(isRoot()).perform(waitId(R.id.activity_login_progress_bar, 2000));
    }

    /**
     * Method that get the Activity instance under test
     * Taken from https://qathread.blogspot.com/2014/09/discovering-espresso-for-android-how-to.html
     * @return ACtivity object containing activity under test
     */
    private Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(() -> {
            Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
            if (resumedActivities.iterator().hasNext()){
                currentActivity = (Activity) resumedActivities.iterator().next();
            }
        });

        return currentActivity;
    }

    private void ifLoggedInLogOut() {
        if (session.isLoggedIn()) {
            onView(withId(R.id.event_picking_login_account)).perform(click());
            onView(withId(R.id.layout_login_signup_logout_button)).perform(click());
            SystemClock.sleep(1000);
        }
    }

}