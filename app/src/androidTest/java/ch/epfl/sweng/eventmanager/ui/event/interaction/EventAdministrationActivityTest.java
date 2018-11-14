package ch.epfl.sweng.eventmanager.ui.event.interaction;

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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onIdle;

@RunWith(AndroidJUnit4.class)
public class EventAdministrationActivityTest {
    @Rule
    public final ActivityTestRule<EventAdministrationActivity> mActivityRule =
            new ActivityTestRule<>(EventAdministrationActivity.class);

    @Test
    public void testNavigation() {
        Intent intent = new Intent();
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 1);
        mActivityRule.launchActivity(intent);

        // Open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onIdle();

        // Switch to the 'visitor' section
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_showcase));

        onIdle();
    }
}