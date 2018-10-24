package ch.epfl.sweng.eventmanager.ui.eventShowcase.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

import static android.support.test.espresso.Espresso.onIdle;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class EventFormFragmentTest {

    @Rule
    public final ActivityTestRule<EventShowcaseActivity> mActivityRule =
            new ActivityTestRule<>(EventShowcaseActivity.class);

    @Test
    public void testGoToForm() {
        Intent intent = new Intent();
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 1);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.contact_form_go_button)).check(matches(isClickable()))
                .perform(ViewActions.click());

        //assertTrue(getActivity().equals(EventFormFragment.class));

        onView(withId(R.id.contact_form_send_button)).check(matches(isClickable()))
                .perform(ViewActions.click());

        onView(withText(R.string.contact_form_empty_field)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        fillInAndClick(R.id.contact_form_name, "John Snow");

        fillInAndClick(R.id.contact_form_subject, "Knowledge is power");

        fillInAndClick(R.id.contact_form_content, "I know nothing...");
    }

    private void fillInAndClick(int id, String text) {
        onView(withId(id)).perform(ViewActions.typeText(text));

        onView(withId(R.id.contact_form_send_button)).perform(ViewActions.click());
        onView(withText(R.string.contact_form_empty_field)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    public Context getActivity() {
        Log.i("Test", InstrumentationRegistry.getTargetContext().toString());
        return InstrumentationRegistry.getTargetContext();
    }

}