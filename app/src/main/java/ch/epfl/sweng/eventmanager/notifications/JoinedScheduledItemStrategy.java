package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

/**
 * Alerts the user 10mn before a scheduling item starts
 */
public class JoinedScheduledItemStrategy extends NotificationStrategy<ScheduledItem> {
    private static final Integer TEN_MINUTES = 600000; // 10 mn in millis
    private static final String titleText = " will start soon";

    public JoinedScheduledItemStrategy(Context context) {
        super(context);
    }

    @Override
    public void scheduleNotification(ScheduledItem scheduledItem) {
        // get Notification based on scheduled item
        Notification notification = getNotificationFromScheduleItem(scheduledItem);

        SchedulerHelper.scheduleNotification(context, scheduledItem.getId().hashCode(), notification, SchedulerHelper.getTimeTo(scheduledItem.getDate()) - TEN_MINUTES);
    }

    @Override
    public void unscheduleNotification(ScheduledItem scheduledItem) {
        SchedulerHelper.unscheduleNotification(context, scheduledItem.getId().hashCode());
    }

    private Notification getNotificationFromScheduleItem(ScheduledItem scheduledItem) {
        return NotificationBuilder.getNotificationFrom(context, scheduledItem.getArtist() + " " + scheduledItem.getItemType() + titleText, scheduledItem.getDescription());
    }
}
