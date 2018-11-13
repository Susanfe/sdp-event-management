package ch.epfl.sweng.eventmanager.ui.userManager;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import junit.framework.AssertionFailedError;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public final ActivityTestRule<EventPickingActivity> mActivityRule =
            new ActivityTestRule<>(EventPickingActivity.class);
    int MAX_RETRY_COUNT = 10;

    @Before
    public void disableFirebaseAuth() {
        Session.enforceDummySessions();
    }

    @Before
    public void setup() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void testSuccessfulLogin() {
        String email = "lamb.da@domain.tld";
        String password = "secret";

        onView(ViewMatchers.withId(R.id.email_field)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.password_field)).perform(typeText(password)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        SystemClock.sleep(1000);

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            try {
                SystemClock.sleep(1000);
                onView(withId(R.id.sign_in_progress_bar)).check(matches(isDisplayed()));
                break;
            } catch (AssertionFailedError e) {
                Log.w("testSuccessfulLogin", "Waiting for authentication...");
            } catch (NoMatchingViewException e) {
                // If the view does not exist, we switched to another activity!
                break;
            }
        }

        onView(withId(R.id.main_text)).check(matches(withText(containsString(email))));

        //return to main activity
        pressBack();
        pressBack();
        //click on my account
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.login_button)).perform(click());
        pressBack();
        //logout
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.logout_button)).perform(click());
    }


    @Test
    @Ignore("Not handled by the dummy authentication")
    public void testWrongCredentials() {
        String email = "lamb.da@domain.tld";
        String password = "wrong";
        // FIXME: find a way to get the error message from Firebase
        String invalidCredentialError = "The password is invalid or the user does not have a password.";


        onView(withId(R.id.email_field)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.password_field)).perform(typeText(password)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            try {
                SystemClock.sleep(1000);
                onView(withId(R.id.sign_in_progress_bar)).check(matches(isDisplayed()));
                break;
            } catch (AssertionFailedError e) {
                Log.w("testSuccessfulLogin", "Waiting for authentication...");
            } catch (NoMatchingViewException e) {
                // If the view does not exist, we switched to another activity!
                break;
            }
        }

        onView(withId(R.id.password_field)).check(matches(hasErrorText(invalidCredentialError)));
    }

    @Test
    public void testInvalidCredentialsInput() {
        String email = "al.pha";
        String password = "secret";
        String emptyPasswordError = getResourceString(R.string.empty_password_activity_login);
        String invalidEmailError = getResourceString(R.string.invalid_email_activity_login);

        // Test empty password
        onView(withId(R.id.email_field)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.password_field)).check(matches(hasErrorText(emptyPasswordError)));

        // Test invalid email address
        onView(withId(R.id.password_field)).perform(typeText(password)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.email_field)).check(matches(hasErrorText(invalidEmailError)));
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
