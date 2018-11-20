package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.User;

public final class Session {
    private static InMemorySession session;

    /**
     * Used to lazily access the session since InMemoryFirebaseSession blows up when
     * instantiated out of the emulator.
     */
    private static InMemorySession getSession() {
       if (session == null) session = new InMemoryFirebaseSession();
       return session;
    }

    /**
     * Used in tests to bypass Firebase Auth which is broken in our CI.
     */
    public static void enforceDummySessions() {
        session = new DummyInMemorySession();
    }

    public static User getCurrentUser() {
       return getSession().getCurrentUser();
    }

    public static boolean isLoggedIn() {
       return getSession().isLoggedIn();
    }

    public static void login(String email, String password, Activity context, OnCompleteListener callback) {
        getSession().login(email, password, context, callback);
    }

    public static void registerAndLogin(String email, String password, Activity context, OnCompleteListener callback) {
        getSession().registerAndLogin(email, password, context, callback);
    }

    public static void logout() {
        getSession().logout();
    }

    /**
     * Check whether the current session is allowed to access an event given a clearance level.
     * @param role requested clearance
     * @param ev event to be accessed
     * @return true is the user is cleared, false otherwise
     */
    public static boolean isClearedFor(Role role, Event ev) {
        if (!isLoggedIn()) return false;
        if (ev == null || ev.getPermissions() == null) return false;
        Map<Role, List<String>> roleToUidMap = ev.getPermissions();

        String currentUid = getCurrentUser().getUid();
        return (roleToUidMap.containsKey(role) && roleToUidMap.get(role).contains(currentUid));
    }
}