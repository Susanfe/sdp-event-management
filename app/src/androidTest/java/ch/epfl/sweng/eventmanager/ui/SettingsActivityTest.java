package ch.epfl.sweng.eventmanager.ui;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.settings.SettingsActivity;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.PreferenceMatchers.withKey;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

public class SettingsActivityTest {
    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void testSettings() {
        String soundKey = mActivityRule.getActivity().getString(R.string.key_ticket_scanning_sounds);
        String vibrationKey = mActivityRule.getActivity().getString(R.string.key_ticket_scanning_vibrations);
        // Check if is displayed
        onData(allOf(is(instanceOf(Preference.class)), withKey(soundKey))).check(matches(isDisplayed()));
        // Perform click
        onData(allOf(is(instanceOf(Preference.class)), withKey(soundKey))).onChildView(withClassName(is(Switch.class.getName()))).perform(click());

        onData(allOf(is(instanceOf(Preference.class)), withKey(vibrationKey))).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(Preference.class)), withKey(vibrationKey))).onChildView(withClassName(is(Switch.class.getName()))).perform(click());
    }
}
