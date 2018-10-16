package ch.epfl.sweng.eventmanager.userManagement;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Optional;

import ch.epfl.sweng.eventmanager.repository.data.User;

/**
 * Provides a static interface to session management.
 */
public final class Session {
    private static InMemorySession session = InMemorySession.getInstance();

    // Private constructor to disable instantiation (only contains static methods)
    private Session() {}

    /**
     * Check the credentials of an user and set local session if successful. Any existing session
     * is destroyed.
     * @param user user to authenticate against
     * @param password password to check
     * @return true if the login was successful
     */
    public static boolean login(User user, String password) {
        if (user.checkPassword(password)) {
            session.setCurrentUser(user);
            return true;
        } else {
            session.reset();
            return false;
        }
    }

    /**
     * Destroy the current session.
     */
    public static void logout() {
        session.reset();
    }

    /**
     * @return true if an user is currently logged in
     */
    public static boolean isLoggedIn() {
        return (session.getUser() != null);
    }

    /**
     * @return an Optional containing the logged user
     */
    public static User getCurrentUser() {
       return session.getUser();
    }
}
