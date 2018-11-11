package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
}
