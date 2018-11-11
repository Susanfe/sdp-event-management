package ch.epfl.sweng.eventmanager.userManagement;

import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ch.epfl.sweng.eventmanager.repository.data.Event;
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

    public static boolean isClearedFor(Role role, Event ev) {

        if (ev == null) return false;
        Map<String, List<String>> uidMap = ev.getUsers();
        if (uidMap == null) return false;

        String currentUid = getCurrentUser().getUid();
        String convertedRole = role.toString().toLowerCase();
        return (uidMap.containsKey(convertedRole) && uidMap.get(convertedRole).contains(currentUid));
    }
}