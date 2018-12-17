package ch.epfl.sweng.eventmanager.repository.impl;

import android.util.Log;
import ch.epfl.sweng.eventmanager.notifications.CloudMessageEventStrategy;
import ch.epfl.sweng.eventmanager.notifications.NotificationRequestResponse;
import ch.epfl.sweng.eventmanager.notifications.NotificationScheduler;
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
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null)
            NotificationScheduler.scheduleNotification(new NotificationRequestResponse(notification.getTitle(), notification.getBody(), remoteMessage.getFrom()), new CloudMessageEventStrategy(getApplicationContext()));
    }

    /**
     * Subscribe the user to notifications specific to {@param ev}
     */
    public static void subscribeToNotifications(Event ev) {
        FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(ev));
    }

    /**
     * Unsubscribe the user to notifications specific to {@param ev}
     */
    public static void unsubscribeFromNotifications(Event ev) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(ev));
    }

    private static String getTopicName(Event ev) {
        return String.valueOf(ev.getId());
    }
}
