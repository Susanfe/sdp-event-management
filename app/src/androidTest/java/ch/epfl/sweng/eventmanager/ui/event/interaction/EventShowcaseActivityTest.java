package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.Manifest;
import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.rule.GrantPermissionRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.RecyclerViewButtonClick;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.allOf;

public class EventShowcaseActivityTest {
    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule = new EventTestRule<>(EventShowcaseActivity.class);

    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @After
    public void remove() {
        mActivityRule.finishActivity();
        Intents.release();
    }

    @Before
    public void startIntents() {
        Intents.init();
    }

    @Test
    public void testNavigation() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_map));

        onIdle();
        SystemClock.sleep(200);
        //test back navigation
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());


        SystemClock.sleep(200);
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_schedule));

        pressBack();


        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_map));
        pressBack();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_contact));
        pressBack();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        pressBack();
    }

    @Test
    public void openEventPicker() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));

        String help_text = getResourceString(R.string.help_text_go_join_events);
        onView(withId(R.id.event_picking_help_text)).check(matches(withText(help_text)));

    }

    private String getResourceString(int id) {
        // FIXME use non deprecated methods over the following one
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

    @Test
    public void joinEventTest() {
        onView(withId(R.id.menu_showcase_activity_join_switch))
                .perform(click());
        onView(withId(R.id.menu_showcase_activity_join_id))
                .perform(click());
    }


    @Test
    public void testEventPicking() {
        Intents.release(); // Don't capture start intent

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));

        SystemClock.sleep(200);

        // Click on an item and capture start intent
        Intents.init();

        onView(withId(R.id.event_picking_bottom_sheet_text)).perform(click());

        onView(withId(R.id.not_joined_event_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                RecyclerViewButtonClick.clickChildViewWithId(R.id.goto_event_btn)));

        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(EventShowcaseActivity.class.getName()),
                IntentMatchers.hasExtraWithKey(EventPickingActivity.SELECTED_EVENT_ID)));

        Intents.assertNoUnverifiedIntents();

        // Leave the Intents initialized as it will be closed by the @After method

    }

    @Test
    public void testJoinEvent() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));

        SystemClock.sleep(200);

        onView(withId(R.id.event_picking_bottom_sheet_text)).perform(click());

        onView(withId(R.id.not_joined_event_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                RecyclerViewButtonClick.clickChildViewWithId(R.id.join_event_btn)));

        onView(allOf(withId(R.id.snackbar_text), withText(R.string.event_successfully_joined))).check(matches(isDisplayed()));

        SystemClock.sleep(200);

        onView(allOf(withText(R.string.undo))).perform(click());

        onView(withId(R.id.event_picking_bottom_sheet_text)).perform(click());

        onView(withId(R.id.joined_events_list)).check(matches(isDisplayed()));

    }

    @Test
    public void testDoubleBackToExit() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));
        pressBack();
        pressBack();
    }

    @Test
    public void singleBackShouldNotExit() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_pick_event));
        pressBack();
        onView(withText(getResourceString(R.string.double_back_press_to_exit))).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
}
