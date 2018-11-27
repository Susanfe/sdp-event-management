package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.app.Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.users.Session;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;

public class EventPickingActivityTest {

    private String email = "lamb.da@domain.tld";
    private String password = "secret";
    private Activity currentActivity;

    @Rule
    public final EventTestRule<EventPickingActivity> mActivityRule = new EventTestRule<>(EventPickingActivity.class);

    @Before
    public void disableFirebaseAuth() {
        Session.enforceDummySessions();
        Session.logout();
    }

    @After
    public void autoLogOut() {
        Session.logout();
    }

    @Test
    public void testLoginFeature() {
        onView(withId(R.id.event_picking_login_account)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertLoggedUIstate(ViewMatchers.Visibility.GONE);
        assertNotLoggedUIstate(ViewMatchers.Visibility.VISIBLE);

        pressBack();
    }

    @Test
    public void testLogoutFeature() {
        Session.login(email, password, getActivityInstance(), task -> {});

        pressBack();

        onView(withId(R.id.event_picking_login_account)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertLoggedUIstate(ViewMatchers.Visibility.VISIBLE);
        assertNotLoggedUIstate(ViewMatchers.Visibility.GONE);

        pressBack();
    }

    private void assertLoggedUIstate(ViewMatchers.Visibility state) {
        onView(withId(R.id.layout_login_signup_logged)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_logout_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_account_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));
    }

    private void assertNotLoggedUIstate(ViewMatchers.Visibility state) {
        onView(withId(R.id.layout_login_signup_not_logged)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_login_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));

        onView(withId(R.id.layout_login_signup_signup_button)).check(ViewAssertions.matches(
                withEffectiveVisibility(state)));
    }

    private Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(() -> {
            Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
            if (resumedActivities.iterator().hasNext()){
                currentActivity = (Activity) resumedActivities.iterator().next();
            }
        });

        return currentActivity;
    }

}