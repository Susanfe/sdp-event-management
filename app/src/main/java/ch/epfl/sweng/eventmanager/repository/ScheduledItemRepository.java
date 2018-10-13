package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class ScheduledItemRepository {
    private static final String TAG = "ScheduledItemRepository";

    @Inject
    public ScheduledItemRepository() {
    }

    public LiveData<List<ScheduledItem>> getConcerts(int eventId) {
        final MutableLiveData<List<ScheduledItem>> data = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("schedule_items")
                .child("event_" + eventId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<ScheduledItem>> typeToken = new GenericTypeIndicator<List<ScheduledItem>>() {
                };

                List<ScheduledItem> scheduledItems = dataSnapshot.getValue(typeToken);
                data.postValue(scheduledItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting schedule for event " + eventId, databaseError.toException());
            }
        });

        return data;
    }
}
