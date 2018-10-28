package ch.epfl.sweng.eventmanager.userManagement;

import ch.epfl.sweng.eventmanager.repository.data.User;

public interface InMemorySession {
    void login();
    User getCurrentUser();
    void logout();
    boolean isLoggedIn();
}
