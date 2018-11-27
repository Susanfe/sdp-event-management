package ch.epfl.sweng.eventmanager.ui;

import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.fragments.settings.SettingsActivity;
import org.junit.Rule;
import org.junit.Test;

public class SettingsActivityTest {
    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void testSettings() {
        String soundKey = mActivityRule.getActivity().getString(R.string.key_ticket_scanning_sounds);
        String vibrationKey = mActivityRule.getActivity().getString(R.string.key_ticket_scanning_vibrations);
/*        // Check if is displayed
        onData(allOf(is(instanceOf(Preference.class)), withKey(soundKey))).check(matches(isDisplayed()));
        // Perform click
        onData(allOf(is(instanceOf(Preference.class)), withKey(soundKey))).onChildView(withClassName(is(Switch.class.getName()))).perform(click());

        onData(allOf(is(instanceOf(Preference.class)), withKey(vibrationKey))).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(Preference.class)), withKey(vibrationKey))).onChildView(withClassName(is(Switch.class.getName()))).perform(click());*/
    }
}
