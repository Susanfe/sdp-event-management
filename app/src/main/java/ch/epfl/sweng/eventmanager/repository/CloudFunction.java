package ch.epfl.sweng.eventmanager.repository;

import com.google.android.gms.tasks.Task;

/**
 * @author Louis Vialar
 */
public interface CloudFunction {

    /**
     * Calls a dedicated FireBase Cloud Function allowing an event administrator to add an user to
     * its event.
     *
     * @param email   email of the target user
     * @param eventId target event
     * @param role    string representation role to be assigned to the target user
     * @return the related task
     */
    Task<Boolean> addUserToEvent(String email, int eventId, String role);
}
