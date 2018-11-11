package ch.epfl.sweng.eventmanager.users;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import ch.epfl.sweng.eventmanager.repository.data.FirebaseBackedUser;
import ch.epfl.sweng.eventmanager.repository.data.User;

@Singleton
public class InMemoryFirebaseSession implements InMemorySession{
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void login() {
    }

    @Override
    public User getCurrentUser() {
        if (mAuth.getCurrentUser() == null) return null;
        else return new FirebaseBackedUser(mAuth.getCurrentUser());
    }

    @Override
    public boolean isLoggedIn() {
        return (mAuth.getCurrentUser() != null);
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }
}
