package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import javax.inject.Singleton;

import ch.epfl.sweng.eventmanager.repository.data.DummyUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.ui.user.DisplayAccountActivity;

/**
 * Dummy InMemorySession class, only used in tests.
 */
@Singleton
public class DummyInMemorySession implements InMemorySession{
    /**
     * Public in order to be used from the tests.
     */
    public static final String DUMMY_EMAIL = "lamb.da@domain.tld";
    public static final String DUMMY_PASSWORD = "secret";
    public static final String DUMMY_UID = "u0YmYQasWpNaNYZt4iXngV0aTxF3";
    public static final String DUMMY_DISPLAYNAME = "Lamb Da";

    private DummyUser user;

    @Override
    public void login(String email, String password, Activity context, OnCompleteListener callback) {
        if (email.equals(DUMMY_EMAIL) && password.equals(DUMMY_PASSWORD)) {
            user = new DummyUser(DUMMY_UID,DUMMY_DISPLAYNAME, DUMMY_EMAIL);

            // Switch to DisplayAccountActivity if successfully authenticated
            if (context != null) {
                Intent intent = new Intent(context, DisplayAccountActivity.class);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void registerAndLogin(String email, String password, Activity context, OnCompleteListener callback) {
        // Nothing to do
    }

    @Override
    public User getCurrentUser() {
        return user;
    }

    @Override
    public boolean isLoggedIn() {
        return (user != null);
    }

    @Override
    public void logout() {
        user = null;
    }
}
