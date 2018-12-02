package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.Gravity;
import android.view.View;
import android.widget.RatingBar;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.AndroidJUnitRunner;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.RecyclerViewButtonClick;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.repository.LiveDataTestUtil;
import ch.epfl.sweng.eventmanager.repository.data.EventRating;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockFeedbackRepository;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventFeedbackFragmentTest {
    private static final Integer EVENT_ID = 1;

    private static final String DESCRIPTION_1 = "The event was great !";
    private static final String DESCRIPTION_2 = "The event was awful !";
    private static final Float RATING_1 = 5f;
    private static final Float RATING_2 = 0f;

    @Inject
    MockFeedbackRepository repository;

    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class);

    @Before
    public void setUp() {
        TestApplication.component.inject(this);
    }

    @Test
    public void submitFeedbackTest() {
        repository.cleanRatings();
        onView(withId(R.id.feedback_for_go_button)).check(matches(isClickable())).perform(click());
        onView(withId(R.id.feedback_submit_feedback)).check(matches(isClickable())).perform(click());

        submitRating(DESCRIPTION_1, RATING_1);
        onView(withText(R.string.event_feedback_submitted)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void submitFeedbackTwiceFailsTest() {
        repository.cleanRatings();
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_feedback));
        onView(withId(R.id.feedback_submit_feedback)).check(matches(isClickable())).perform(click());

        submitRating(DESCRIPTION_1, RATING_1);

        onView(withId(R.id.feedback_for_go_button)).check(matches(isClickable())).perform(click());
        onView(withId(R.id.feedback_submit_feedback)).check(matches(isClickable())).perform(click());

        submitRating(DESCRIPTION_2, RATING_2);

        onView(withText(R.string.event_feedback_already_submitted)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @After public void close(){
        mActivityRule.finishActivity();
    }

    private void submitRating(String content, Float rating) {
        onView(withId(R.id.feedback_form_content)).perform(typeText(content), closeSoftKeyboard());
        onView(withId(R.id.feedback_form_ratingBar)).perform(new SetRating(rating));
        onView(withId(R.id.feedback_form_send_button)).perform(ViewActions.click());
    }

    private final class SetRating implements ViewAction {
        private final float rating;

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
