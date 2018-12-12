package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Custom FirebaseMessagingService handles received remote message from Firebase Cloud Messaging Service and alerts the
 * user with a notification.
 *
 * @see RemoteMessage
 */
public class FirebaseNotificationService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseNotif_Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Received a message : " + remoteMessage);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedPreferences.getBoolean(getApplicationContext().getString(R.string.key_settings_notifications_switch), true)) {
            RemoteMessage.Notification notif = remoteMessage.getNotification();

            if (notif != null) {
                String targetTopics = remoteMessage.getFrom();
                if (targetTopics != null) {
                    Notification notification = NotificationBuilder.getFirebaseNotificationFrom(getApplicationContext(), notif.getTitle(), notif.getBody(), extractEventId(targetTopics));
                    SchedulerHelper.scheduleNotification(getApplicationContext(), notif.hashCode(), notification, 0);
                }
            }
        }
    }

    public static void subscribeToNotifications(Event ev) {
        FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(ev));
    }

    public static void unsubscribeFromNotifications(Event ev) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(ev));
    }

    private static int extractEventId(String to) {
        String topic = to.replace("/topics/", "");
        return Integer.parseInt(topic);
    }

    private static String getTopicName(Event ev) {
        return String.valueOf(ev.getId());
    }
}
