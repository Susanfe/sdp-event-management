package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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