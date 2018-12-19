package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InMemoryFirebaseSession implements InMemorySession {
    private final FirebaseAuth mAuth;

    @Inject
    public InMemoryFirebaseSession() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password, Activity context, OnCompleteListener callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context, callback);
    }

    @Override
    public void registerAndLogin(String email, String password, Activity context, OnCompleteListener callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(context, callback);
    }

    @Override
    public String getCurrentUserUid() {
        if (mAuth.getCurrentUser() == null) {
            return null;
        } else {
            return mAuth.getCurrentUser().getUid();
        }
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
