package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.RatingBar;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockFeedbackRepository;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.not;

public class EventFeedbackFragmentTest {

    @Inject
    MockFeedbackRepository repository;

    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class);

    @Before
    public void setUp() {
        TestApplication.component.inject(this);
        Intents.init();

        onView(withId(R.id.feedback_for_go_button)).perform(click());
        Intents.intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void submitFeedback() {
        submitRating("The event was great !", 3f);
    }

    private void submitRating(String content, Float rating){
        onView(withId(R.id.feedback_form_content)).perform(typeText(content), closeSoftKeyboard());
        onView(withId(R.id.feedback_form_ratingBar)).perform(new SetRating(rating));
        onView(withId(R.id.feedback_form_send_button)).perform(click());
    }

    private final class SetRating implements ViewAction {
        private float rating;

        SetRating(float rating) {
            this.rating = rating;
        }

        @Override
        public Matcher<View> getConstraints() {
            return ViewMatchers.isAssignableFrom(RatingBar.class);
        }

        @Override
        public String getDescription() {
            return "Custom view action to set rating.";
        }

        @Override
        public void perform(UiController uiController, View view) {
            RatingBar ratingBar = (RatingBar) view;
            ratingBar.setRating(rating);
        }
    }


}
