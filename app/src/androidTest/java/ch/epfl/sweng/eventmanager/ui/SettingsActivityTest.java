package ch.epfl.sweng.eventmanager.ui;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoActivityResumedException;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.settings.SettingsActivity;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static junit.framework.TestCase.fail;

public class SettingsActivityTest {
    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void testBackPress() {
        try {
            Espresso.pressBack();
            fail("Should have thrown NoActivityResumedException");
        } catch (NoActivityResumedException expected) {
        }
    }

    @Test()
    public void testUpArrow() {
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    }
}
