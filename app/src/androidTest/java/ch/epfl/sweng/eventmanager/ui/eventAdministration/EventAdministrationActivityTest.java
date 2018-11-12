package ch.epfl.sweng.eventmanager.ui.eventAdministration;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.eventmanager.ui.event.interaction.EventAdministrationActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import static androidx.test.espresso.Espresso.onIdle;

@RunWith(AndroidJUnit4.class)
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