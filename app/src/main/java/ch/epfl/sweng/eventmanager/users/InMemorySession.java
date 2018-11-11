package ch.epfl.sweng.eventmanager.users;

import ch.epfl.sweng.eventmanager.repository.data.User;

public interface InMemorySession {
    void login();

    /**
     * Get you any currently logged user.
     *
     * @return an User, or null
     */
    User getCurrentUser();

    /**
     * Log out any currently logged user.
     */
    void logout();

    /**
     * @return true if there currently is an user logged in
     */
    boolean isLoggedIn();
}
