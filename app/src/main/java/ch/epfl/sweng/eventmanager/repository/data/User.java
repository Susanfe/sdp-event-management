package ch.epfl.sweng.eventmanager.repository.data;

import com.google.firebase.auth.FirebaseUser;

public final class User {
    private String uid;
    private String email;
    private String displayName;

    public User() {
        // Used for dependency injection
    }

    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }

    public User(FirebaseUser firebaseUser) {
        this.uid = firebaseUser.getUid();
    }

    public void setUid(String uid) { this.uid = uid; }

    public String getUid() { return uid; }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
