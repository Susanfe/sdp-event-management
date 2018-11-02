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

public class NotificationBuilder {

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

    static Notification getNotification(@NonNull Context context, ScheduledItem scheduledItem) {
        // Set up on tap action TODO display my_schedule fragment when tap on notification
        PendingIntent pendingIntent = toEventPickingActivity(context);

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationScheduler.CHANNEL_ID)
                .setContentTitle(scheduledItem.getArtist() + " concert will start soon")
                .setContentText(scheduledItem.getDescription())
                .setSmallIcon(R.drawable.ic_marker_active) // TODO define a small icon for notifications
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }

    static Notification getNotification(@NonNull Context context, Event event){
        // Set up on tap action
        PendingIntent pendingIntent = toEventPickingActivity(context);

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationScheduler.CHANNEL_ID)
                .setContentTitle(event.getName() + " will start tomorrow")
                .setContentText(event.getDescription())
                .setSmallIcon(R.drawable.ic_marker_active)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }
}
