package ch.epfl.sweng.eventmanager.repository.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.eventmanager.repository.data.Ticket;

public class FirebaseCloudFunction {
    /**
     * Calls a dedicated FireBase Cloud Function allowing an event administrator to add an user to
     * its event.
     *
     * @param email email of the target user
     * @param eventId target event
     * @param role string representation role to be assigned to the target user
     * @return the related task
     */
    public static Task<Boolean> addUserToEvent(String email, int eventId, String role) {
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

    /**
     * Import a list of tickets into the firebase RealTime database.
     *
     * @param tickets list of tickets to import
     * @param eventId target event
     * @return the related task
     */
    public static Task<Boolean> importTickets(List<Ticket> tickets, int eventId) {
        Map<String, Object> data = new HashMap<>();
        // TODO: set parameters

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("importTickets")
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
}
