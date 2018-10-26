package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.data.Event;
import ch.epfl.sweng.eventmanager.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.data.Spot;
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
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference("events");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Event>> typeToken = new GenericTypeIndicator<List<Event>>() {
                };

                List<Event> events = dataSnapshot.getValue(typeToken);
                if (events != null) {
                    events.remove(null); // Somehow the list might contain a null element
                }

                data.postValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting events.", databaseError.toException());
            }
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

        return ret;
    }

    private String getImageName(Event event) {
        Log.d(TAG, event.getName());
        return event.getName().replace(" ", "_") + ".png";
    }

    public LiveData<Bitmap> getEventImage(Event event) {
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("events-logo");
        StorageReference eventLogoReference = imagesRef.child(getImageName(event));
        final MutableLiveData<Bitmap> img = new MutableLiveData<>();

        final long ONE_MEGABYTE = 1024 * 1024;
        eventLogoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Log.d(TAG, "Image is loaded");
            img.setValue(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options));
        }).addOnFailureListener(exception -> Log.w(TAG, "Could not load " + event.getName() + " image"));
        return img;
    }

    private <T> LiveData<List<T>> getElems(int eventId, String basePath, GenericTypeIndicator<List<T>> token) {
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

    public LiveData<List<Spot>> getSpots(int eventId) {
        return this.getElems(eventId, "spots", new GenericTypeIndicator<List<Spot>>() {});
    }

    public LiveData<List<ScheduledItem>> getScheduledItems(int eventId) {
        return this.getElems(eventId, "schedule_items", new GenericTypeIndicator<List<ScheduledItem>>() {});
    }
}
