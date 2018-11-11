package ch.epfl.sweng.eventmanager.userManagement;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;

import ch.epfl.sweng.eventmanager.repository.data.User;

public final class Session {
    private static InMemorySession session = new InMemoryFirebaseSession();

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
}