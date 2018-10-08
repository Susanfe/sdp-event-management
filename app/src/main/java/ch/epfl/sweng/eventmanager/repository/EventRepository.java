package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import com.google.firebase.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class EventRepository {

    @Inject
    public EventRepository() {
    }

    public LiveData<List<Event>> getEvents() {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("events");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Event>> typeToken = new GenericTypeIndicator<List<Event>>() {
                };

                List<Event> events = dataSnapshot.getValue(typeToken);
                if (events != null)
                    events.remove(null); // Somehow the list might contain a null element

                data.postValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return data;
    }

    public LiveData<Event> getEvent(int eventId) {
        MutableLiveData<Event> ret = new MutableLiveData<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId));

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ret.postValue(dataSnapshot.getValue(Event.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return ret;
    }
}
