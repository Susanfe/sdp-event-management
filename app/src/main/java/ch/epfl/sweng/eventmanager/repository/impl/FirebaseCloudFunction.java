package ch.epfl.sweng.eventmanager.repository.impl;

import ch.epfl.sweng.eventmanager.notifications.NotificationRequest;
import ch.epfl.sweng.eventmanager.repository.CloudFunction;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

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
                    return (Boolean) Objects.requireNonNull(task.getResult()).getData();
                });
    }

    /**
     * Calls a dedicated FireBase Cloud Function allowing an event administrator to remove a role
     * from an user on its event.
     *
     * @param uid key of the uid of the user to be removed
     * @param eventId target event
     * @param role string representation of the role to be removed
     * @return the related task
     */

    public Task<Boolean> removeUserFromEvent(String uid, int eventId, String role) {
        // Prepare parameters for the Firebase Cloud Function
        Map<String, Object> data = new HashMap<>();
        data.put("eventId", eventId);
        data.put("uid", uid);
        data.put("role", role);
        data.put("push", true);

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("removeUserFromEvent")
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return (Boolean) Objects.requireNonNull(task.getResult()).getData();
                });
    }

    /**
     * Calls a dedicated FireBase Cloud Function allowing an event administrator to send a notification to all users
     * having joined a specified event
     * @param notificationRequest request by the event administrator
     * @return the related task
     * @see NotificationRequest
     */
    public Task<Boolean> sendNotificationToUsers(NotificationRequest notificationRequest){

        Map<String, Object> data = new HashMap<>();
        data.put("title", notificationRequest.getTitle());
        data.put("body", notificationRequest.getBody());
        data.put("eventId", notificationRequest.getEventId());

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("sendNotificationToUsers")
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return (Boolean) Objects.requireNonNull(task.getResult()).getData();
                });
    }
}
