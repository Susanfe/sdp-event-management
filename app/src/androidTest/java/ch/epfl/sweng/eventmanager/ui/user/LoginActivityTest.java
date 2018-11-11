package ch.epfl.sweng.eventmanager.ui.user;

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

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.AssertionFailedError;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    int MAX_RETRY_COUNT = 20;

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
        SystemClock.sleep(1000);

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            try {
                SystemClock.sleep(1000);
                onView(withId(R.id.sign_in_progress_bar))
                        .check(matches(isDisplayed()));
                break;
            } catch (AssertionFailedError e) {
                Log.w("testSuccessfulLogin", "Waiting for authentication...");
            } catch(NoMatchingViewException e) {
                // If the view does not exist, we switched to another activity!
                break;
            }
        }

        onView(withId(R.id.main_text))
                .check(matches(withText(containsString(email))));
    }

    @Test
    public void testWrongCredentials() {
        String email = "lamb.da@domain.tld";
        String password = "wrong";
        // FIXME: find a way to get the error message from Firebase
        String invalidCredentialError = "The password is invalid or the user does not have a password.";


        onView(withId(R.id.email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            try {
                SystemClock.sleep(1000);
                onView(withId(R.id.sign_in_progress_bar))
                        .check(matches(isDisplayed()));
                break;
            } catch (AssertionFailedError e) {
                Log.w("testSuccessfulLogin", "Waiting for authentication...");
            } catch(NoMatchingViewException e) {
                // If the view does not exist, we switched to another activity!
                break;
            }
        }

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