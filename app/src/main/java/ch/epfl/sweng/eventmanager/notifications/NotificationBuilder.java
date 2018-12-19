package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ui.event.interaction.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

/**
 * Helper class.
 * Provides tools to create a {@link Notification} from a ScheduledItem and Event.
 */
class NotificationBuilder {

    /**
     * Provides a {@link PendingIntent} to EventPickingActivity View
     *
     * @param context non null
     * @return PendingIntent
     */
    private static PendingIntent toEventPickingActivity(Context context) {
        Intent intent = new Intent(context, EventPickingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    /**
     * Provides a {@link PendingIntent} to EventMainFragment View of a specific event
     *
     * @param context non null
     * @param eventId id of the event
     * @return PendingIntent
     */
    private static PendingIntent toEventMainFragment(Context context, int eventId) {
        Intent intent = new Intent(context, EventShowcaseActivity.class);
        intent.putExtra(EventPickingActivity.SELECTED_EVENT_ID, eventId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    /**
     * Build a Notification from {@param title} and {@param description} and sets on tap behavior to EventPickingActivity
     * @param context non null
     * @return a instance of Notification.class
     * @see Notification
     */
    static Notification getNotificationFrom(@NonNull Context context, String title, String description) {
        // Set up on tap action TODO send to event my_schedule fragment
        PendingIntent pendingIntent = toEventPickingActivity(context);

        // Create a notification
        return getNotification(context, title, description, pendingIntent);
    }

    /**
     * Build a Notification from {@param title} and {@param description} and sets on tap behavior to EventFeedbackFragment
     * of an event from {@param eventId}
     * @param context non null
     * @return a instance of Notification.class
     * @see Notification
     */
    static Notification getFeedbackNotificationFrom(@NonNull Context context, String title, String description, int eventId) {
        PendingIntent pendingIntent = toEventMainFragment(context, eventId);

        // Create a notification
        return getNotification(context, title, description, pendingIntent);
    }

    /**
     * Build a Notification from {@param title} and {@param description} and sets on tap behavior to EventMainFragment
     * of an event from {@param eventId}
     * @param context non null
     * @return a instance of Notification.class
     * @see Notification
     */
    static Notification getFirebaseNotificationFrom(@NonNull Context context, String title, String description, int eventId) {
        // todo make it send to event showcase activity / bug with event feedback
        PendingIntent pendingIntent = toEventMainFragment(context, eventId);

        // Create a notification
        return getNotification(context, title, description, pendingIntent);
    }

    private static Notification getNotification(@NonNull Context context, String title, String description, PendingIntent pendingIntent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SchedulerHelper.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }
}
