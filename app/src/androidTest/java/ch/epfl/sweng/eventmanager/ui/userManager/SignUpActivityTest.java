package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.TestHelper;
import ch.epfl.sweng.eventmanager.users.Session;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Before
    public void disableFirebaseAuth() {
        Session.enforceDummySessions();
    }

    @Rule
    public final ActivityTestRule<SignUpActivity> mActivityRule =
            new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void testNonMatchingPasswords() {
        String email = "al.pha";
        String password = "secret";
        String passwordMatchError = getResourceString(R.string.password_match_error);

        // Test non-matching passwords
        onView(withId(R.id.email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password_confirmation_field))
                .perform(typeText("secret+typo"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.signup_button)).perform(click());
        onView(withId(R.id.password_field))
                .check(matches(hasErrorText(passwordMatchError)));
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
