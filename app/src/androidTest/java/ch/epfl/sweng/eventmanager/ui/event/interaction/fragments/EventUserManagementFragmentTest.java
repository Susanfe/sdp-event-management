package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.view.Gravity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventAdministrationActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EventUserManagementFragmentTest {
    @Rule
    public final EventTestRule<EventAdministrationActivity> mActivityRule =
            new EventTestRule<>(EventAdministrationActivity.class);

    @Before
    public void setup() {
        TestApplication.component.inject(this);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_user_management));
    }

    /**
     * Fails due to the User not being injected yet.
    @Test
    public void testAddUser() {
        onView(withId(R.id.user_managament_user_mail_field))
                .perform(typeText("non-existing@domain.tld"))
                .perform(closeSoftKeyboard());

        onView(withId(R.id.user_management_add_user_button))
                .check(matches(isClickable()))
                .perform(click());
    }
    */
}
