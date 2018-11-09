package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;

/**
 * Provides tools to create a {@link Notification} from a ScheduledItem and Event
 */
public class NotificationBuilder {

    static Notification getNotificationFromScheduleItem(@NonNull Context context, ScheduledItem scheduledItem) {
        return getNotificationFromItem(context, scheduledItem.getArtist() + " concert will start soon", scheduledItem.getDescription());
    }

    static Notification getNotificationFromEvent(@NonNull Context context, Event event){
        return getNotificationFromItem(context, event.getName() + " will start tomorrow", event.getDescription());
    }

    static Notification getNotificationFromNewScheduledItem(@NonNull Context context){
        //TODO implement this
        return null;
    }

    /**
     * Provides a {@link PendingIntent} to EventPickingActivity.class when you tap on a notification
     *
     * @param context non null
     * @return PendingIntent
     */
    private static PendingIntent toEventPickingActivity(Context context){
        Intent intent = new Intent(context, EventPickingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private static Notification getNotificationFromItem(@NonNull Context context, String title, String description){
        // Set up on tap action TODO send to event my_schedule fragment
        PendingIntent pendingIntent = toEventPickingActivity(context);

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationScheduler.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_marker_active)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }
}
