package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class SpotsRepository {
    private static final String TAG = "SpotsRepository";

    @Inject
    public SpotsRepository() {
    }

    public LiveData<List<Spot>> getSpots(int eventId) {
        final MutableLiveData<List<Spot>> data = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("spots")
                .child("event_" + eventId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Spot>> typeToken = new GenericTypeIndicator<List<Spot>>() {
                };

                List<Spot> spots = dataSnapshot.getValue(typeToken);
                data.postValue(spots);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting schedule for event " + eventId, databaseError.toException());
            }
        });

        return data;
    }
}
