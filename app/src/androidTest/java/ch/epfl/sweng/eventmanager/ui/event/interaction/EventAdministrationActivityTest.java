package ch.epfl.sweng.eventmanager.ui.event.interaction;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EventAdministrationActivityTest {
    @Rule
    public final EventTestRule<EventAdministrationActivity> mActivityRule =
            new EventTestRule<>(EventAdministrationActivity.class);

    @Test
    public void testNavigation() {
        // Open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onIdle();

        // Switch to the 'visitor' section
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_showcase));

        onIdle();
    }

    @Test
    public void testOpenEdition() {
        // Open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onIdle();

        // Switch to the 'event edition' section
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_event));

        onIdle();
    }

    @Test
    public void testOpenSchedule() {
        // Open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onIdle();

        // Switch to the 'event edition' section
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_schedule));

        onIdle();
    }
}