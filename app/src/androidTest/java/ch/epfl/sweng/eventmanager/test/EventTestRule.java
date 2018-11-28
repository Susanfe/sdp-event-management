package ch.epfl.sweng.eventmanager.test;

import android.app.Activity;
import android.content.Intent;

import androidx.test.rule.ActivityTestRule;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

/**
 * @author Louis Vialar
 */
public class EventTestRule<T extends Activity> extends ActivityTestRule<T> {
    private final int eventId;

    public EventTestRule(Class<T> activityClass) {
        this(activityClass, 1);
    }

    public EventTestRule(Class<T> activityClass, int eventId) {
        super(activityClass);

        this.eventId = eventId;
    }

    @Override
    protected Intent getActivityIntent() {
        Intent intent = new Intent();
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventId);
        return intent;
    }
}
