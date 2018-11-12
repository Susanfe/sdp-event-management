package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.User;

public final class Session {
    private static InMemorySession session = new InMemoryFirebaseSession();

    /**
     * Used in tests to bypass Firebase Auth which is broken in our CI.
     */
    public static void enforceDummySessions() {
        session = new DummyInMemorySession();
    }

    public static User getCurrentUser() {
       return session.getCurrentUser();
    }

    public static boolean isLoggedIn() {
       return session.isLoggedIn();
    }

    public static void login(String email, String password, Activity context, OnCompleteListener callback) {
        session.login(email, password, context, callback);
    }

    public static void logout() {
        session.logout();
    }

    /**
     * Check whether the current session is allowed to access an event given a clearance level.
     * @param role requested clearance
     * @param ev event to be accessed
     * @return true is the user is cleared, false otherwise
     */
    public static boolean isClearedFor(Role role, Event ev) {
        if (!isLoggedIn()) return false;
        if (ev == null || ev.getUsers() == null) return false;
        Map<String, List<String>> roleToUidMap = ev.getUsers();

        String currentUid = getCurrentUser().getUid();
        String rawRole = role.toString().toLowerCase();
        return (roleToUidMap.containsKey(rawRole) && roleToUidMap.get(rawRole).contains(currentUid));
    }
}