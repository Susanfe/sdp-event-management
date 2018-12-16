package ch.epfl.sweng.eventmanager.repository;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Ticket;

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

    /**
     * Calls a dedicated FireBase Cloud Function allowing an event administrator to remove a role
     * from an user on its event.
     *
     * @param uid uid of the user to be removed
     * @param eventId target event
     * @param role string representation of the role to be removed
     * @return the related task
     */
    Task<Boolean> removeUserFromEvent(String uid, int eventId, String role);

    /**
     * Import a list of tickets into the firebase RealTime database.
     *
     * @param tickets list of tickets to import
     * @param eventId target event
     * @return the related task
     */
    public Task<Boolean> importTickets(List<Ticket> tickets, int eventId);
}
