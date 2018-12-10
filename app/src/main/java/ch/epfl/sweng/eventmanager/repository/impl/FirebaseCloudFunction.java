package ch.epfl.sweng.eventmanager.repository.impl;

import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class FirebaseCloudFunction implements CloudFunction {

    @Inject
    public FirebaseCloudFunction() {

    }

    /**
     * Calls a dedicated FireBase Cloud Function allowing an event administrator to add an user to
     * its event.
     *
     * @param email   email of the target user
     * @param eventId target event
     * @param role    string representation role to be assigned to the target user
     * @return the related task
     */
    public Task<Boolean> addUserToEvent(String email, int eventId, String role) {
        // Prepare parameters for the Firebase Cloud Function
        Map<String, Object> data = new HashMap<>();
        data.put("eventId", eventId);
        data.put("userEmail", email);
        data.put("role", role);
        data.put("push", true);

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("addUserToEvent")
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    // TODO handle null pointer exception
                    Boolean result = (Boolean) task.getResult().getData();
                    return result;
                });
    }

    public Task<Void> sendNotificationToUsers(String title, String body, Event event) {

        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);
        notification.put("eventId", event.getId());
        notification.put("eventName", event.getName());

        return FirebaseHelper.publishElement(event.getId(), "notificationRequest", notification);
    }
}
