package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public class EventRepository {
    private static String TAG = "EventRepository";

    @Inject
    public EventRepository() {
    }

    public LiveData<List<Event>> getEvents() {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("events");
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("events-logo");

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
                Log.w(TAG, "Error when getting events.", databaseError.toException());
            }
        });

        Transformations.map(data, events -> {
            for (Event event : events) {
                event.setImage(loadEventLogo(imagesRef, event));
            }
            return events;
        });

        return data;
    }

    public LiveData<Event> getEvent(int eventId) {
        final MutableLiveData<Event> ret = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase
                .getInstance()
                .getReference("events")
                .child(String.valueOf(eventId));

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ret.postValue(dataSnapshot.getValue(Event.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting event " + eventId + ".", databaseError.toException());
            }
        });

        final StorageReference imagesRef = FirebaseStorage.getInstance().getReference("events-logo");
        Transformations.map(ret, ev -> {
            ev.setImage(loadEventLogo(imagesRef, ev));
            return ev;
        });

        return ret;
    }

    private String getImageName(Event event){
        return event.getName().replace(" ", "_");
    }

    private Bitmap loadEventLogo(StorageReference imagesRef, Event event) {
        StorageReference eventLogoReference = imagesRef.child(getImageName(event));
        final Bitmap[] img = new Bitmap[1];
        img[0] = null;

        final long ONE_MEGABYTE = 1024 * 1024;
        eventLogoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            img[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }).addOnFailureListener(exception -> Log.e(TAG, "Could not load then event image"));
        return img[0];
    }
}
