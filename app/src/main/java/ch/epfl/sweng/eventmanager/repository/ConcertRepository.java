package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Concert;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class ConcertRepository {
    private static final String TAG = "ConcertRepository";

    @Inject
    public ConcertRepository() {
    }

    public LiveData<List<Concert>> getConcerts(int eventId) {
        final MutableLiveData<List<Concert>> data = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("schedule_items")
                .child("event_" + eventId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Concert>> typeToken = new GenericTypeIndicator<List<Concert>>() {
                };

                List<Concert> concerts = dataSnapshot.getValue(typeToken);
                data.postValue(concerts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting schedule for event " + eventId, databaseError.toException());
            }
        });

        return data;
    }
}
