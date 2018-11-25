package ch.epfl.sweng.eventmanager.users;

import android.app.Activity;
import ch.epfl.sweng.eventmanager.repository.data.FirebaseBackedUser;
import ch.epfl.sweng.eventmanager.repository.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

@Singleton
public class InMemoryFirebaseSession implements InMemorySession {
    private final FirebaseAuth mAuth;
    private FirebaseBackedUser user;

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
    public User getCurrentUser() {
        if (mAuth.getCurrentUser() == null) {
            user = null;
        } else if (user == null || !user.getUid().equals(mAuth.getCurrentUser().getUid())) {
            user = new FirebaseBackedUser(mAuth.getCurrentUser());
        }
        return user;
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
