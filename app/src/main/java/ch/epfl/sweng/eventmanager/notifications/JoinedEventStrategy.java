package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.Event;

/**
 * Alerts the user a day before an event starts
 */
public class JoinedEventStrategy extends NotificationStrategy<Event> {
    private static final Integer ONE_DAY = 86_400_000; // 24h in millis
    private static final String titleText = " will start tomorrow";


    public JoinedEventStrategy(Context context) {
        super(context);
    }

    @Override
    public void scheduleNotification(Event event) {
        // get Notification based on scheduled item
        Notification notification = getNotificationFromEvent(event);

        SchedulerHelper.scheduleNotification(context, event.getId(), notification, SchedulerHelper.getTimeTo(event.getBeginDateAsDate()) - ONE_DAY);
    }

    @Override
    public void unscheduleNotification(Event event) {
        SchedulerHelper.unscheduleNotification(context, event.getId());
    }

    private Notification getNotificationFromEvent(Event event) {
        return NotificationBuilder.getNotificationFromItem(context, event.getName() + titleText, event.getDescription());
    }
}
