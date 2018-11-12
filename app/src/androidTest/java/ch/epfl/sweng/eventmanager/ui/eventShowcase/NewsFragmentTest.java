package ch.epfl.sweng.eventmanager.ui.eventShowcase;

import android.os.SystemClock;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.repository.data.DummyUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockNewsRepository;
import ch.epfl.sweng.eventmanager.test.userManagement.TestInMemorySession;
import ch.epfl.sweng.eventmanager.userManagement.Session;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.*;

public class NewsFragmentTest {
    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule =
            new EventTestRule<>(EventShowcaseActivity.class, 1);

    @Inject
    MockNewsRepository repository;


    private String newsTitle = "A sweet news";
    private String newsContent = "This is the news content. Sweet, right?";

    @Before
    public void setup() {
        TestInMemorySession.enable();
        TestInMemorySession.instance.setCurrentUser(new DummyUser("1", "admin", "admin", new HashSet<>(Arrays.asList(User.Permission.values()))));

        TestApplication.component.inject(this);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        // Switch displayed fragment
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_news));

    }

    private void createNews() {
        SystemClock.sleep(500);

        onView(withId(R.id.news_create_button))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        SystemClock.sleep(100);

        onView(withId(R.id.title)).perform(typeText(newsTitle), closeSoftKeyboard());
        onView(withId(R.id.content)).perform(typeText(newsContent), closeSoftKeyboard());
        onView(withId(R.id.send)).perform(ViewActions.click());
    }

    @Test
    public void testCreateNews() {
        createNews();

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