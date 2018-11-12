package ch.epfl.sweng.eventmanager.ui;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventMapFragmentTest {

    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

    @Test
    public void eventMapTest() {
        Intent intent = new Intent();
        // Opens Sysmic Event
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 2);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        // Display Schedule Events
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
        onView(withId(R.id.text_test)).check(matches(withText("everything is ready")));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_main));
        SystemClock.sleep(800);
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));
        SystemClock.sleep(800);
    }
}
