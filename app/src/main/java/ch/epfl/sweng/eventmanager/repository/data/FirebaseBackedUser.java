package ch.epfl.sweng.eventmanager.repository.data;

import com.google.firebase.auth.FirebaseUser;

public final class FirebaseBackedUser implements User {
    FirebaseUser firebaseUser;

    public FirebaseBackedUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    @Override
    public String getDisplayName() {
        return firebaseUser.getDisplayName();
    }

    @Override
    public String getEmail() {
        return firebaseUser.getEmail();
    }

    @Override
    public String getUid() { return firebaseUser.getUid(); }

    @Override
    public boolean hasPermission(Permission permission) {
        // TODO: replace by the real permission system
        return true; // the permission system will not be implemented this way. This is the placeholder permission system.
    }
}
