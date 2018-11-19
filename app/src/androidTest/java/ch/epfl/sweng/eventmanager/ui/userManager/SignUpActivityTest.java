package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.TestHelper;
import ch.epfl.sweng.eventmanager.users.Session;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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

    @Test
    public void testValidInput() {
        String email = "al.pha@domain.tld";
        String password = "secret";

        onView(withId(R.id.email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password_confirmation_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.signup_button)).perform(click());

        // Nothing is going to happen since we use the DummyInMemorySession
        // Check that everything went fine with the lack of error messages
        onView(withId(R.id.email_field))
                .check(matches(TestHelper.hasNoErrorText()));
        onView(withId(R.id.password_field))
                .check(matches(TestHelper.hasNoErrorText()));
        onView(withId(R.id.password_confirmation_field))
                .check(matches(TestHelper.hasNoErrorText()));
    }

    private String getResourceString(int id) {
        // FIXME use non deprecated method instead of following one
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
