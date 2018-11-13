package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.view.Gravity;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.ticketing.ErrorCodes;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.ScanningTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingConfigurationPickerActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingTestRule;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Louis Vialar
 */
public class FailingPickerTest extends ScanningTest {
    @Rule
    public final EventTestRule<EventShowcaseActivity> mActivityRule = new EventTestRule<>(EventShowcaseActivity.class, 2);

    public FailingPickerTest() {
        super(2);
    }

    @Before
    public void openActivity() {
        getOrCreateTicketingService(mActivityRule.getActivity()).failNextWith(Collections.singletonList(new ApiResult.ApiError(ErrorCodes.PERMS_MISSING.getCode())));

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_scan));
    }


    @Test
    public void testFailsCorrectly() {
        SystemClock.sleep(200);

        onView(withId(R.id.pick_config)).check(matches(withText(Matchers.startsWith(
                mActivityRule.getActivity().getString(R.string.loading_failed).split(":")[0]
        ))));
    }
}
