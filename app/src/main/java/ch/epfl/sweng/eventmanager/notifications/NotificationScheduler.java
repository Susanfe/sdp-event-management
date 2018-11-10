package ch.epfl.sweng.eventmanager.notifications;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class providing an interface to Notification handling.
 * <p>
 * For now, two possible notifications can be scheduled :
 * - 10mn before a scheduled item starts
 * - A day before an event start
 */
public class NotificationScheduler {
    static final String CHANNEL_ID = "notify_001";
    private static final String CHANNEL_NAME = "Scheduled Notifications";
    private static final AtomicReference<Boolean> isNotificationChannelSet = new AtomicReference<>(false);
    private static final Integer TEN_MINUTES = 600000; // 10 mn in millis
    private static final Integer ONE_DAY = 86_400_000; // 24h in millis

    /**
     * Schedule a new Notification based on given {@link ScheduledItem}. The notification is broadcast
     * to {@link NotificationPublisher} and is displayed TEN_MINUTES before the item starts.
     *
     * @param context       not null
     * @param scheduledItem Scheduled Item
     */
    public static void scheduleNotification(@NonNull Context context, ScheduledItem scheduledItem) {
        // get Notification based on scheduled item
        Notification notification = NotificationBuilder.getNotificationFromScheduleItem(context, scheduledItem);

        scheduleNotification(context, scheduledItem.getId().hashCode(), notification, getTimeTo(scheduledItem.getDate()) - TEN_MINUTES);
    }

    /**
     * Schedule a new Notification based on given {@link Event}. The notification is broadcast
     * to {@link NotificationPublisher} and is displayed ONE_DAY before the event starts.
     * Reference Date is {@link #getTimeTo(Date)} with begin date of the event
     *
     * @param context not null
     * @param event   planned event
     */
    public static void scheduleNotification(@NonNull Context context, Event event) {
        // get Notification based on scheduled item
        Notification notification = NotificationBuilder.getNotificationFromEvent(context, event);

        scheduleNotification(context, event.getId(), notification, getTimeTo(event.getBeginDate()) - ONE_DAY);
    }

    /**
     * Schedule {@param notification} to be displayed in {@param delay} milliseconds.
     *
     * @param context non null
     * @param itemId  unique id to notification
     */
    private static void scheduleNotification(@NonNull Context context, int itemId, Notification notification, long delay) {
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
     * Deletes the Notification bind to {@link ScheduledItem}
     *
     * @param context       not null
     * @param scheduledItem Scheduled Item
     */
    public static void unscheduleNotification(@NonNull Context context, ScheduledItem scheduledItem) {
        unscheduleNotification(context, scheduledItem.getId().hashCode());
    }

    /**
     * Deletes the Notification bind to {@link Event}
     *
     * @param context not null
     * @param event   planned event
     */
    public static void unscheduleNotification(@NonNull Context context, Event event) {
        unscheduleNotification(context, event.getId());
    }

    private static void unscheduleNotification(@NonNull Context context, int itemId) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), itemId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        // Cancel the `PendingIntent` after you've canceled the alarm
        pendingIntent.cancel();
    }

    /**
     * Compute the time remaining until {@param date}
     *
     * @param date, not null
     * @return time in milliseconds
     * @throws NullPointerException  if the date is null
     * @throws IllegalStateException if {@param date} is already past
     */
    private static long getTimeTo(Date date) {
        if (date == null)
            throw new NullPointerException();

        long timeUntil = date.getTime() - System.currentTimeMillis();
        Log.d("NOTIFICATION_PUBLISHER", String.format("date time = %s and current time = %s", date.getTime(), System.currentTimeMillis()));

        if (timeUntil < 0) // Event is past
            return Long.MAX_VALUE;
        else return timeUntil;
    }

    private static void setupNotificationChannel(@NonNull Context context) {

        if (!isNotificationChannelSet.get()) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Setup Notification Channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            isNotificationChannelSet.set(true);
        }
    }
}
