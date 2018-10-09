package ch.epfl.sweng.eventmanager.ui.userManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testSuccessfulLogin() {
        String email = "lamb.da@domain.tld";
        String password = "secret";

        onView(ViewMatchers.withId(R.id.email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.main_text))
                .check(matches(withText(containsString(email))));
    }

    @Test
    public void testWrongCredentials() {
        String email = "al.pha@domain.tld";
        String password = "secret";
        String invalidCredentialError = getResourceString(
                R.string.invalid_credentials_activity_login
        );

        onView(withId(R.id.email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.password_field))
                .check(matches(hasErrorText(invalidCredentialError)));
    }

    @Test
    public void testInvalidCredentialsInput() {
        String email = "al.pha";
        String password = "secret";
        String emptyPasswordError = getResourceString(R.string.empty_password_activity_login);
        String invalidEmailError = getResourceString(R.string.invalid_email_activity_login);

        // Test empty password
        onView(withId(R.id.email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.password_field))
                .check(matches(hasErrorText(emptyPasswordError)));

        // Test invalid email address
        onView(withId(R.id.password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.email_field))
                .check(matches(hasErrorText(invalidEmailError)));
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
