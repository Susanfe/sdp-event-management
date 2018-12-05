package ch.epfl.sweng.eventmanager.repository.impl;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.UserRepository;
import ch.epfl.sweng.eventmanager.repository.data.User;

public class FirebaseUserRepository implements UserRepository {
    private static final String TAG = "FirebaseUserRepository";
    private static final String FIREBASE_REF = "users";

    @Override
    public LiveData<User> getUser(String uid) {
        final MutableLiveData<User> ret = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(FIREBASE_REF)
                .child(uid);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ret.postValue(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting user " + uid + ".", databaseError.toException());
            }
        });

        return ret;
    }
}
