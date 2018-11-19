package ch.epfl.sweng.eventmanager.repository.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class FirebaseHelper {
    public static <T> LiveData<List<T>> getList(DatabaseReference dbRef, Class<T> classOfT) {
        final MutableLiveData<List<T>> data = new MutableLiveData<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> events = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren())
                    events.add(child.getValue(classOfT));

                data.postValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseHelper", "Error when getting data list.", databaseError.toException());
            }
        });

        return data;
    }

    public static <T> LiveData<T> getElement(DatabaseReference dbRef, Class<T> classOfT) {
        final MutableLiveData<T> data = new MutableLiveData<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T elem = dataSnapshot.getValue(classOfT);

                data.postValue(elem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FirebaseHelper", "Error when getting data element.", databaseError.toException());
            }
        });

        return data;
    }

    public static <T> Task<Void> publishElement(int eventId, String ref, T elem){
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(ref)
                .child("event_" + eventId)
                .push(); // Create a new key in the list

        return dbRef.setValue(elem);
    }
}
