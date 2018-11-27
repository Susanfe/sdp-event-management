package ch.epfl.sweng.eventmanager.repository;

import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.repository.data.ScheduledItem;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;
import ch.epfl.sweng.eventmanager.repository.impl.FirebaseHelper;
import ch.epfl.sweng.eventmanager.repository.internal.DatabaseReferenceWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseDatabaseWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.FirebaseStorageWrapper;
import ch.epfl.sweng.eventmanager.repository.internal.StorageReferenceWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Louis Vialar
 */
@Singleton
public final class EventRepository {
    private static String TAG = "EventRepository";

    private final FirebaseDatabaseWrapper db;
    private final FirebaseStorageWrapper storage;

    @Inject
    EventRepository(FirebaseDatabaseWrapper db, FirebaseStorageWrapper storage) {
        this.db = db;
        this.storage = storage;
    }

    public LiveData<List<Event>> getEvents() {
        DatabaseReferenceWrapper dbRef = db.getReference("events");

        return Transformations.switchMap(FirebaseHelper.getList(dbRef, Event.class), list -> {
            MediatorLiveData<List<Event>> events = new MediatorLiveData<>();
            List<Event> eventList = new ArrayList<>();
            for (Event event : list) {
                events.addSource(getEventImage(event), img -> {
                    event.setImage(img);
                    eventList.add(event);
                    events.setValue(eventList);
                });
            }
            return events;
        });
    }

    public LiveData<Event> getEvent(int eventId) {
        final MutableLiveData<Event> ret = new MutableLiveData<>();
        DatabaseReferenceWrapper dbRef = db.getReference("events")
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

        return Transformations.switchMap(ret, ev -> Transformations.map(getEventImage(ev), img -> {
            ev.setImage(img);
            return ev;
        }));
    }

    private String getImageName(Event event) {
        Log.d(TAG, event.getName());
        return event.getName().replace(" ", "_") + ".png";
    }

    public LiveData<Bitmap> getEventImage(Event event) {
        StorageReferenceWrapper imagesRef = storage.getReference("events-logo");
        StorageReferenceWrapper eventLogoReference = imagesRef.child(getImageName(event));

        return FirebaseHelper.getImage(eventLogoReference);
    }

    private <T> LiveData<List<T>> getElems(int eventId, String basePath, Class<T> classOfT) {
        DatabaseReferenceWrapper dbRef = db.getReference(basePath)
                .child("event_" + eventId);

        return FirebaseHelper.getList(dbRef, classOfT);
    }

    public LiveData<List<Spot>> getSpots(int eventId) {
        DatabaseReferenceWrapper dbRef = db.getReference("spots").child("event_" + eventId);

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

    public LiveData<List<ScheduledItem>> getScheduledItems(int eventId) {
        return this.getElems(eventId, "schedule_items", ScheduledItem.class);
    }

    public LiveData<List<Zone>> getZones(int eventId) {
        return this.getElems(eventId, "zones", Zone.class);
    }

    private String getImageName(Spot spot) {
        Log.d(TAG, spot.getTitle());
        return spot.getTitle().replace(" ", "_") + ".jpg";
    }

    public LiveData<Bitmap> getSpotImage(Spot spot) {
        StorageReferenceWrapper imagesRef = storage.getReference("spots-pictures");
        StorageReferenceWrapper spotImageReference = imagesRef.child(getImageName(spot));

        return FirebaseHelper.getImage(spotImageReference);
    }
}
