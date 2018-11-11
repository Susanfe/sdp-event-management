package ch.epfl.sweng.eventmanager.notifications;

import android.util.Log;

/**
 * An interface to notification handling.
 * This and Notification Strategies are the only public accessible's.
 */
public class NotificationScheduler {

    /**
     * Schedule a new Notification based on given {@link NotificationStrategy}.
     *
     * @param item                 additional information
     * @param notificationStrategy defines the scheduling
     */
    public static <I> void scheduleNotification(I item, NotificationStrategy<I> notificationStrategy) {
        notificationStrategy.scheduleNotification(item);
        Log.d("HELLO", "WAS HERE");
    }

    /**
     * Deletes the Notification bind to {@param item}
     */
    public static <I> void unscheduleNotification(I item, NotificationStrategy<I> notificationStrategy) {
        notificationStrategy.unscheduleNotification(item);
    }
}
