package ch.epfl.sweng.eventmanager.repository.impl;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author Louis Vialar
 */
@Singleton
public class FirebaseEventRepository implements EventRepository {
    private static String TAG = "EventRepository";
    private static final Random randGen = new SecureRandom();

    /**
     * This method generates an ID for a new event.
     * We assume that 31 bits of entropy is enough to avoid most collisions (probability = 4.6566129e-10)
     */
    private static int generateEventId() {
        return randGen.nextInt(Integer.MAX_VALUE);
    }

    @Inject
    FirebaseEventRepository() {
    }

    @Override
    public LiveData<List<Event>> getEvents() {
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference("events");
        dbRef.keepSynced(true);

        return FirebaseHelper.getList(dbRef, Event.class, (event, ref) -> {
            event.setId(Integer.parseInt(ref.getKey()));
            return event;
        });
    }

    @Override
    public LiveData<Event> getEvent(int eventId) {
        final MutableLiveData<Event> event = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("events").child(String.valueOf(eventId));

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event.postValue(dataSnapshot.getValue(Event.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Error when getting event " + eventId + ".", databaseError.toException());
            }
        });

        return event;
    }

    private String getImageName(Event event) {
        Log.d(TAG, event.getName());
        return event.getName().replace(" ", "_") + ".png";
    }

    private <T> LiveData<List<T>> getElems(int eventId, String basePath, Class<T> classOfT) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(basePath).child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, classOfT);
    }


    @Override
    public LiveData<List<Spot>> getSpots(int eventId) {
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference("spots").child("event_" + eventId);

        return Transformations.switchMap(FirebaseHelper.getList(dbRef, Spot.class), list -> {
            MediatorLiveData<List<Spot>> events = new MediatorLiveData<>();
            List<Spot> spotList = new ArrayList<>();
            for (Spot spot : list) {
                events.addSource(getSpotImage(spot), img -> {
                    spot.setImage(img);
                    spotList.add(spot);
                    events.setValue(spotList);
                });
            }
            return events;
        });
    }

    @Override
    public LiveData<List<ScheduledItem>> getScheduledItems(int eventId) {
        return this.getElems(eventId, "schedule_items", ScheduledItem.class);
    }

    @Override
    public LiveData<List<Zone>> getZones(int eventId) {
        return this.getElems(eventId, "zones", Zone.class);
    }

    private String getImageName(Spot spot) {
        Log.d(TAG, spot.getTitle());
        return spot.getTitle().replace(" ", "_") + ".jpg";
    }

    @Override
    public LiveData<Bitmap> getSpotImage(Spot spot) {
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("spots-pictures");
        StorageReference spotImageReference = imagesRef.child(getImageName(spot));
        return FirebaseHelper.getImage(spotImageReference);
    }

    @Override
    public Task<Event> createEvent(Event event) {
        event.setId(generateEventId());
        return updateEvent(event);
    }

    @Override
    public void uploadImage(Event event, Uri imageSrc) {
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("events-logo");
        StorageReference eventsLogoRef = imagesRef.child(event.getImageName());
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference("events").child(String.valueOf(event.getId()));
        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/webp").build();
        FirebaseHelper.uploadFileToStorage(eventsLogoRef, imageSrc, metadata)
                .addOnSuccessListener(taskSnapshot -> eventsLogoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            event.setImageURL(uri.toString());
            Map<String, Object> updateUrl = new HashMap<>();
            updateUrl.put("imageURL", uri.toString());
            dbRef.updateChildren(updateUrl);
        }));
    }

    @Override
    public Task<Event> updateEvent(Event event) {
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference("events").child(String.valueOf(event.getId()));
        return dbRef.setValue(event).continueWith((v) -> {
            v.getResult(); // Forward exceptions
            return event;
        });
    }

    public Task deleteEvent(Event event) {
        if(event == null) {
            throw new IllegalArgumentException("Event to delete cannot be null");
        }
        String eventMainRef = String.valueOf(event.getId());
        String eventSubRef = "event_" + eventMainRef;
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        String[] dbPaths = {"news","spots","zones","schedule_items","ratings","notificationRequest"};
        return fdb.getReference("events").child(eventMainRef).removeValue().addOnSuccessListener(s -> {
            for(String path : dbPaths) {
                fdb.getReference(path).child(eventSubRef).removeValue();
            }
            if(event.hasAnImage()) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(event.getImageURL());
                storageReference.delete().addOnFailureListener( m -> {
                    Log.d(TAG,"Failed to delete image for event" + eventMainRef);
                    m.getMessage();
                        });
            }
        });
    }
}
