package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;
import ch.epfl.sweng.eventmanager.repository.data.FirebaseBackedUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InMemoryFirebaseSession implements InMemorySession {
    @Inject
    InMemoryFirebaseSession() {}


    @Override
    public void login(String email, String password, Activity context, OnCompleteListener callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, callback);
    }

    @Override
    public User getCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return null;
        else return new FirebaseBackedUser(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    public boolean isLoggedIn() {
        return (FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
