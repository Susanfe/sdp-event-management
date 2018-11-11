package ch.epfl.sweng.eventmanager.notifications;

import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

/**
 * Alerts the user that a new scheduling item was added to an event
 */
public class NewScheduledItemStrategy extends NotificationStrategy<ScheduledItem> {

    public NewScheduledItemStrategy(Context context) {
        super(context);
    }

    @Override
    public void scheduleNotification(ScheduledItem scheduledItem) {

    }

    @Override
    public void unscheduleNotification(ScheduledItem scheduledItem) {

    }
}
