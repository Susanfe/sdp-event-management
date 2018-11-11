package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventMapFragmentTest {

    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class, 2);

    @Before
    public void setup() {
        sleep(100);

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        sleep(100);

        // Display Schedule Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
    }

    @Test
    public void eventMapTest() {
        onView(withId(R.id.text_test)).check(matches(withText("everything is ready")));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_main));
        sleep(800);
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
        sleep(800);
    }

    @After
    public void finish() {
        mActivityRule.finishActivity();
    }
}
