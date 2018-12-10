package ch.epfl.sweng.eventmanager.repository.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public final class User {
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference userEntry;

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

    public String getUid() { return uid; }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
