package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
public class EventFormFragmentTest {

    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);


    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 1);
        mActivityRule.launchActivity(intent);

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.contact_form_go_button)).check(matches(isClickable()))
                .perform(click());

        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGoToForm() {
        String errorText = getResourceString(R.string.contact_form_error);

        onView(withId(R.id.contact_form_send_button))
                .check(matches(isClickable()))
                .perform(click());

        onView(withId(R.id.contact_form_name)).check(matches(hasErrorText(errorText)));

        fillInAndClick(R.id.contact_form_name,R.id.contact_form_subject, "John Snow", errorText);

        fillInAndClick(R.id.contact_form_subject,R.id.contact_form_content, "Knowledge is power", errorText);

        onView(withId(R.id.contact_form_content)).perform(typeText("I know nothing..."), closeSoftKeyboard());

        onView(withId(R.id.contact_form_send_button)).perform(click());

    }

    private void fillInAndClick(int id, int errorId, String text, String errorText) {
        onView(withId(id)).perform(typeText(text), closeSoftKeyboard());

        onView(withId(R.id.contact_form_send_button)).perform(click());
        onView(withId(errorId)).check(matches(hasErrorText(errorText)));
    }

    // FIXME same method as in LoginActivityTest, we could consider making a TestUtils class although not certain that getTargetContext will work
    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

}