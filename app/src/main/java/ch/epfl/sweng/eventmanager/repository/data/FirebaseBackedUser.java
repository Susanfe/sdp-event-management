package ch.epfl.sweng.eventmanager.repository.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseBackedUser implements User {
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference userEntry;

    private String uid;
    private String email;
    private String displayName;

    private void init() {
        userEntry = db.getReference("/users/" + getUid());
        userEntry.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getKey().equals("email")) {
                        email = (String) dataSnapshot.getValue();
                    } else if (dataSnapshot.getKey().equals("displayName")) {
                        displayName = (String) dataSnapshot.getValue();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                this.onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Ignored
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Ignored
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Ignored
            }
        });
    }

    public FirebaseBackedUser(String uid) {
        this.uid = uid;
        init();
    }

    public FirebaseBackedUser(FirebaseUser firebaseUser) {
        this.uid = firebaseUser.getUid();
        init();
    }

    @Override
    public String getUid() { return uid; }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
