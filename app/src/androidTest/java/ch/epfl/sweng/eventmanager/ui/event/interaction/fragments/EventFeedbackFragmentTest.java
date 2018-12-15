package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.view.View;
import android.widget.RatingBar;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.RecyclerViewButtonClick;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockFeedbackRepository;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class EventFeedbackFragmentTest {
    private static final String DESCRIPTION_1 = "The event was great !";
    private static final Float RATING_1 = 5f;

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
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_feedback));
        onView(withId(R.id.feedback_submit_feedback)).check(matches(isClickable())).perform(click());

        submitRating(DESCRIPTION_1, RATING_1);
        onView(withText(R.string.event_feedback_submitted)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void submitFeedbackAndReadTest() {
        repository.cleanRatings();
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_feedback));
        onView(withId(R.id.feedback_submit_feedback)).check(matches(isClickable())).perform(click());

        submitRating(DESCRIPTION_1, RATING_1);

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_feedback));

        onView(withId(R.id.feedback_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                RecyclerViewButtonClick.clickChildViewWithId(R.id.item_feedback_date)));
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
