package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.User;

import com.google.android.gms.tasks.OnCompleteListener;

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

    /**
     * Used to lazily access the session since InMemoryFirebaseSession blows up when
     * instantiated out of the emulator.
     */
    private InMemorySession getSession() {
        return session;
    }

    public String getCurrentUserUid() {
        return getSession().getCurrentUserUid();
    }

    public boolean isLoggedIn() {
        return getSession().isLoggedIn();
    }

    public void login(String email, String password, Activity context, OnCompleteListener callback) {
        getSession().login(email, password, context, callback);
    }

    public void registerAndLogin(String email, String password, Activity context, OnCompleteListener callback) {
        getSession().registerAndLogin(email, password, context, callback);
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
        if (ev == null || ev.getPermissions() == null) return false;
        Map<Role, List<String>> roleToUidMap = ev.getPermissions();
        String currentUid = getCurrentUserUid();
        // TODO handle null pointer exception
        return (roleToUidMap.containsKey(role) && roleToUidMap.get(role).contains(currentUid));
    }
}