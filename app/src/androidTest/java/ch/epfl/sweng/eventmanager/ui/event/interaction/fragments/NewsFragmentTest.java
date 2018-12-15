package ch.epfl.sweng.eventmanager.ui.event.interaction.fragments;

import android.os.SystemClock;
import android.view.Gravity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockNewsRepository;
import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventAdministrationActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class NewsFragmentTest {
    @Rule
    public final EventTestRule<EventAdministrationActivity> mActivityRule =
            new EventTestRule<>(EventAdministrationActivity.class);

    @Inject
    MockNewsRepository repository;
    @Inject
    Session session;

    private String newsTitle = "A sweet news";
    private String newsContent = "This is the news content. Sweet, right?";

    @Before
    public void setup() {
        TestApplication.component.inject(this);

        session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD, null, null);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_send_news));
    }

    private void createNews() {
        SystemClock.sleep(500);

        onView(withId(R.id.title)).perform(typeText(newsTitle), closeSoftKeyboard());
        onView(withId(R.id.content)).perform(typeText(newsContent), closeSoftKeyboard());
        onView(withId(R.id.send)).perform(click());
    }

    private void openNewsListing() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_showcase));

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_news));
    }

    @Test
    public void testCreateNews() {
        repository.clearNews();
        createNews();
        openNewsListing();

        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(Matchers.allOf(
                hasDescendant(withText(newsTitle)),
                hasDescendant(withText(newsContent))
        ))));
    }

    @Test
    public void testCreateNewsFail() {
        repository.clearNews();
        repository.setNextInsertToFail();
        createNews();

        onView(withText(R.string.send_news_failed)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

}