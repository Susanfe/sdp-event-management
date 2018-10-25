package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import com.google.firebase.database.*;

import java.util.List;

/**
 * @author Louis Vialar
 */
public abstract class AbstractEventDataRepository<T> {
    private static final String TAG = "AbstractEvDataRepo";

    private final String basePath;
    private final GenericTypeIndicator<List<T>> token;

    public AbstractEventDataRepository(String basePath, GenericTypeIndicator<List<T>> token) {
        this.basePath = basePath;
        this.token = token;
    }

    protected LiveData<List<T>> getElems(int eventId) {
        final MutableLiveData<List<T>> data = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(basePath)
                .child("event_" + eventId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> spots = dataSnapshot.getValue(token);
                data.postValue(spots);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting " + basePath + " for event " + eventId, databaseError.toException());
            }
        });

        return data;
    }
}
