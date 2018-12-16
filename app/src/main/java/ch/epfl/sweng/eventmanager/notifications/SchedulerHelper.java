package ch.epfl.sweng.eventmanager.notifications;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.NonNull;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Helper Class.
 * Defines some tools to simplify and avoid code duplication.
 */
class SchedulerHelper {
    private static final AtomicReference<Boolean> isNotificationChannelSet = new AtomicReference<>(false);
    static final String CHANNEL_ID = "notify_001";
    private static final String CHANNEL_NAME = "Scheduled Notifications";

    /**
     * Schedule {@param notification} to be displayed in {@param delay} milliseconds.
     *
     * @param context      non null
     * @param itemId       unique id to notification
     * @param notification to be displayed
     * @param delay        in milliseconds
     */
    static void scheduleNotification(@NonNull Context context, int itemId, Notification notification, long delay) {
        setupNotificationChannel(context);

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.getNotificationId(), itemId);
        notificationIntent.putExtra(NotificationPublisher.getNOTIFICATION(), notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, itemId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (delay < 0) //Special case where the event is past therefore, no notifications are sent
            return;

        long future = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, future, pendingIntent);
    }

    /**
     * Unschedule a notification with specific (@param itemId)
     *
     * @param context non null
     * @param itemId  unique id to notification
     */
    static void unscheduleNotification(@NonNull Context context, int itemId) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), itemId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        // Cancel the `PendingIntent` after you've canceled the alarm
        pendingIntent.cancel();
    }

    /**
     * Starting in Android 8.0 (API level 26), all notifications must be assigned to a channel.
     * @param context non null
     * @link https://developer.android.com/training/notify-user/channels
     * @see NotificationChannel
     */
    private static void setupNotificationChannel(@NonNull Context context) {

        if (!isNotificationChannelSet.get()) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Setup Notification Channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            isNotificationChannelSet.set(true);
        }
    }

    /**
     * Compute the time remaining until {@param date}
     *
     * @param date, not null
     * @return time in milliseconds
     * @throws NullPointerException  if the date is null
     * @throws IllegalStateException if {@param date} is already past
     */
    static long getTimeTo(Date date) {
        if (date == null)
            throw new NullPointerException();

        long timeUntil = date.getTime() - System.currentTimeMillis();

        if (timeUntil < 0) // Event is past
            return Long.MAX_VALUE;
        else return timeUntil;
    }
}
