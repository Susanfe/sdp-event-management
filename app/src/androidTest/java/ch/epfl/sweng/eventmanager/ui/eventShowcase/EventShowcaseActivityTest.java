package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.Espresso.onIdle;

@RunWith(AndroidJUnit4.class)
public class EventShowcaseActivityTest {
    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class);

    @After
    public void remove() {
        mActivityRule.finishActivity();
    }

    @Test
    public void testNavigation() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));

        onIdle();

        //test back navigation
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_schedule));
        pressBack();
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_map));
        pressBack();
    }

    @Test
    public void openEventPicker() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));

        String help_text = getResourceString(R.string.help_text_activity_event_picking);

        onView(withId(R.id.help_text))
                .check(matches(withText(help_text)));
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }


    @Test
    public void testEventPicking() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));

        onView(withText("Event without items")).perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));

        onView(withText("Event with scheduled items")).perform(click());

    }

}
