package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.News;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
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
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference("events");

        return FirebaseHelper.getList(dbRef, Event.class);
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

    private <T> LiveData<List<T>> getElems(int eventId, String basePath, Class<T> classOfT) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(basePath)
                .child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, classOfT);
    }

    public LiveData<List<Spot>> getSpots(int eventId) {
        return this.getElems(eventId, "spots", Spot.class);
    }

    public LiveData<List<ScheduledItem>> getScheduledItems(int eventId) {
        return this.getElems(eventId, "schedule_items", ScheduledItem.class);
    }
    public LiveData<List<Zone>> getZone(int eventId) {
        return this.getElems(eventId, "zone", new GenericTypeIndicator<List<Zone>>() {});
    }
}
