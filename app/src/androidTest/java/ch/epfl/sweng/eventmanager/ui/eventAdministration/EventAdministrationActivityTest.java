package ch.epfl.sweng.eventmanager.ui.eventAdministration;

import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;

import static androidx.test.espresso.Espresso.onIdle;

public class EventAdministrationActivityTest {
    @Rule
    public final ActivityTestRule<EventAdministrationActivity> mActivityRule =
            new ActivityTestRule<>(EventAdministrationActivity.class);

    @Test
    public void testNavigation() {
        Intent intent = new Intent();
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, 1);
        mActivityRule.launchActivity(intent);

        // The administration activity is currently empty

        onIdle();
    }
}