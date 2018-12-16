package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ch.epfl.sweng.eventmanager.R;

public class CloudMessageEventStrategy extends NotificationStrategy<NotificationRequestResponse> {

    public CloudMessageEventStrategy(Context context) {
        super(context);
    }

    @Override
    void scheduleNotification(NotificationRequestResponse not) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Check if the user has disabled notifications globally
        if (sharedPreferences.getBoolean(context.getString(R.string.key_settings_notifications_switch), true)) {

            String targetTopics = not.getFrom();
            if (targetTopics != null) {
                Integer eventId = extractEventId(targetTopics);
                if (eventId != null) {
                    Notification notification = NotificationBuilder.getFirebaseNotificationFrom(context, not.getTitle(), not.getBody(), eventId);
                    SchedulerHelper.scheduleNotification(context, not.hashCode(), notification, 0);
                }
            }
        }
    }

    @Override
    void unscheduleNotification(NotificationRequestResponse not) {
    }

    private static Integer extractEventId(String to) {
        String topic = to.replace("/topics/", "");
        Integer result;
        try {
            result = Integer.decode(topic);
        } catch (NumberFormatException e) {
            return null;
        }
        return result;
    }
}
