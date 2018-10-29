package ch.epfl.sweng.eventmanager.notifications;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;

import java.util.Date;

public class ScheduledItemNotification {
    private static final String CHANNEL_ID = "notify_001";

    public static void scheduleNotification(Context context, ScheduledItem scheduledItem) {

        setupNotificationChannel(context);

        // get Notification based on scheduled item
        Notification notification = getNotification(context, scheduledItem);

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, scheduledItem.getId().hashCode());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + getTimeMillisTo(scheduledItem);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private static long getTimeMillisTo(ScheduledItem scheduledItem) {
        Date scheduledItemDate = scheduledItem.getDate();
        // TODO better handle the cases where Date == null and event is past
        if (scheduledItemDate == null)
            return 0;
        return scheduledItem.getDate().getTime() - System.currentTimeMillis();
    }

    private static Notification getNotification(Context context, ScheduledItem scheduledItem) {
        // Set up on tap action
        Intent intent = new Intent(context, EventShowcaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(scheduledItem.getArtist() + " concert will start soon")
                .setContentText(scheduledItem.getArtist() + " concert will start in 10mn")
                .setSmallIcon(R.drawable.ic_marker_active)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }

    private static void setupNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Setup Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
