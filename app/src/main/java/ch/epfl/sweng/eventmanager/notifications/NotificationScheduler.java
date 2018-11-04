package ch.epfl.sweng.eventmanager.notifications;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class NotificationScheduler {
    static final String CHANNEL_ID = "notify_001";
    private static final String CHANNEL_NAME = "Scheduled Notifications";
    private static final AtomicReference<Boolean> isNotificationChannelSet = new AtomicReference<>(false);
    private static final Integer TEN_MINUTES = 600000; // 10 mn in millis
    private static final Integer ONE_DAY = 86_400_000; // 24h in millis

    /**
     * Create a new Notification based on given {@link ScheduledItem}. The notification is broadcast
     * to {@link NotificationPublisher} and is displayed TEN_MINUTES before the item starts.
     *
     * @param context       not null
     * @param scheduledItem Scheduled Item
     */
    public static void scheduleNotification(@NonNull Context context, ScheduledItem scheduledItem) {
        setupNotificationChannel(context);

        // get Notification based on scheduled item
        Notification notification = NotificationBuilder.getNotification(context, scheduledItem);

        scheduleNotification(context, scheduledItem.getId().hashCode(), scheduledItem.getDate(), notification, TEN_MINUTES);
    }

    /**
     * Create a new Notification based on given {@link Event}. The notification is broadcast
     * to {@link NotificationPublisher} and is displayed ONE_DAY before the event starts.
     * Reference Date is begin date of the event
     *
     * @param context not null
     * @param event   planned event
     */
    public static void scheduleNotification(@NonNull Context context, Event event) {
        setupNotificationChannel(context);

        // get Notification based on scheduled item
        Notification notification = NotificationBuilder.getNotification(context, event);

        scheduleNotification(context, event.getId(), event.getBeginDate(), notification, ONE_DAY);
    }

    private static void scheduleNotification(@NonNull Context context, int itemId, Date date, Notification notification, int delay) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.getNotificationId(), itemId);
        notificationIntent.putExtra(NotificationPublisher.getNOTIFICATION(), notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, itemId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long future = SystemClock.elapsedRealtime() + getTimeTo(date) - delay;
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

        if (timeUntil < 0) // Event is past
            throw new IllegalStateException();
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
