package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public final class Session {
    private final InMemorySession session;

    @Inject
    Session(InMemorySession session) {
        this.session = session;
    }

    private InMemorySession getSession() {
        return session;
    }

    public User getCurrentUser() {
        return getSession().getCurrentUser();
    }

    public boolean isLoggedIn() {
        return getSession().isLoggedIn();
    }

    public void login(String email, String password, Activity context, OnCompleteListener callback) {
        getSession().login(email, password, context, callback);
    }

    public void logout() {
        getSession().logout();
    }

    /**
     * Check whether the current session is allowed to access an event given a clearance level.
     *
     * @param role requested clearance
     * @param ev   event to be accessed
     * @return true is the user is cleared, false otherwise
     */
    public boolean isClearedFor(Role role, Event ev) {
        if (!isLoggedIn()) return false;
        if (ev == null || ev.getUsers() == null) return false;
        Map<String, List<String>> roleToUidMap = ev.getUsers();

        String currentUid = getCurrentUser().getUid();
        String rawRole = role.toString().toLowerCase();
        return (roleToUidMap.containsKey(rawRole) && roleToUidMap.get(rawRole).contains(currentUid));
    }
}