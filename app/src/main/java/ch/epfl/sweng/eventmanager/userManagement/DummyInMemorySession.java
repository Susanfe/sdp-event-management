package ch.epfl.sweng.eventmanager.userManagement;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import javax.inject.Singleton;

import ch.epfl.sweng.eventmanager.repository.data.DummyUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import ch.epfl.sweng.eventmanager.ui.userManager.DisplayAccountActivity;

import java.util.HashSet;

/**
 * Dummy InMemorySession class, only used in tests.
 */
@Singleton
public class DummyInMemorySession implements InMemorySession{
    private final String DUMMY_EMAIL = "lamb.da@domain.tld";
    private final String DUMMY_PASSWORD = "secret";
    private final String DUMMY_UID = "u0YmYQasWpNaNYZt4iXngV0aTxF3";
    private final String DUMMY_DISPLAYNAME = "Lamb Da";

    private DummyUser user;

    @Override
    public void login(String email, String password, Activity context, OnCompleteListener callback) {
        if (email.equals(DUMMY_EMAIL) && password.equals(DUMMY_PASSWORD)) {
            user = new DummyUser(DUMMY_UID,DUMMY_DISPLAYNAME, DUMMY_EMAIL, new HashSet<>());

            // Switch to DisplayAccountActivity if successfully authenticated
            Intent intent = new Intent(context, DisplayAccountActivity.class);
            context.startActivity(intent);
        }
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
