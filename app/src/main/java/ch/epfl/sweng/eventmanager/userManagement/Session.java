package ch.epfl.sweng.eventmanager.userManagement;

import ch.epfl.sweng.eventmanager.repository.data.User;

public final class Session {
    private static InMemorySession session = new InMemoryFirebaseSession();

    public static User getCurrentUser() {
       return session.getCurrentUser();
    }

    public static boolean isLoggedIn() {
       return session.isLoggedIn();
    }

    public static void login() {
        session.login();
    }

    public static void logout() {
        session.logout();
    }
}