package ch.epfl.sweng.eventmanager.ui.user;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.TestHelper;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.users.Session;
import junit.framework.AssertionFailedError;
import org.junit.*;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;

public class LoginActivityTest {
    private final int MAX_RETRY_COUNT = 10;

    @Inject
    Session session;

    @After
    public void autoLogOut() {
        session.logout();
    }

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        TestApplication.component.inject(this);
    }

    @Test
    public void testSuccessfulLogin() {
        String email = "lamb.da@domain.tld";
        String password = "secret";
        SystemClock.sleep(1000);

        onView(ViewMatchers.withId(R.id.activity_login_email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.activity_login_password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        SystemClock.sleep(1000);

        onView(withId(R.id.activity_login_login_button)).perform(click());
        SystemClock.sleep(1000);

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            try {
                SystemClock.sleep(1000);
                onView(withId(R.id.activity_login_progress_bar))
                        .check(matches(isDisplayed()));
                break;
            } catch (AssertionFailedError e) {
                Log.w("testSuccessfulLogin", "Waiting for authentication...");
            } catch(NoMatchingViewException e) {
                // If the view does not exist, we switched to another activity!
                break;
            }
        }

        onView(withId(R.id.display_account_main_text))
                .check(matches(withText(containsString(email))));

        onView(withId(R.id.display_account_logout_button))
                .perform(click());

    }

    @Test
    @Ignore("Not handled by the dummy authentication")
    public void testWrongCredentials() {
        String email = "lamb.da@domain.tld";
        String password = "wrong";
        // FIXME: find a way to create the error message from Firebase
        String invalidCredentialError = "The password is invalid or the user does not have a password.";


        onView(withId(R.id.activity_login_email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.activity_login_password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.activity_login_login_button)).perform(click());

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            try {
                SystemClock.sleep(1000);
                onView(withId(R.id.activity_login_progress_bar))
                        .check(matches(isDisplayed()));
                break;
            } catch (AssertionFailedError e) {
                Log.w("testSuccessfulLogin", "Waiting for authentication...");
            } catch(NoMatchingViewException e) {
                // If the view does not exist, we switched to another activity!
                break;
            }
        }

        onView(withId(R.id.activity_login_password_field))
                .check(matches(hasErrorText(invalidCredentialError)));
    }

    @Test
    public void testInvalidCredentialsInput() {
        String email = "al.pha";
        String password = "secret";
        String emptyPasswordError = getResourceString(R.string.empty_password_error);
        String invalidEmailError = getResourceString(R.string.invalid_email_error);

        // Test empty password
        onView(withId(R.id.activity_login_email_field))
                .perform(typeText(email))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.activity_login_login_button)).perform(click());
        onView(withId(R.id.activity_login_password_field))
                .check(matches(hasErrorText(emptyPasswordError)));

        // Test invalid email address
        onView(withId(R.id.activity_login_password_field))
                .perform(typeText(password))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.activity_login_login_button)).perform(click());
        onView(withId(R.id.activity_login_email_field))
                .check(matches(hasErrorText(invalidEmailError)));
    }

    @Test
    public void testOpenSignUpForm() {
        onView(withId(R.id.activity_login_signup_button))
                .perform(click());

        TestHelper.withToolbarTitle(getResourceString(R.string.title_activity_sign_up));
    }

    private String getResourceString(int id) {
        // FIXME use non deprecated method instead of following one
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }
}
