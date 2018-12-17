package ch.epfl.sweng.eventmanager.repository.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.epfl.sweng.eventmanager.repository.MapEditionRepository;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

@Singleton
public class FirebaseMapEditionRepository implements MapEditionRepository {

    private static final String FIREBASE_REF_ZONES = "zones";
    private static final String FIREBASE_REF_SPOTS = "spots";

    @Inject
    FirebaseMapEditionRepository(){
    }

    @Override
    public Task<List<Zone>> updateZones(int eventId, List<Zone> zones) {
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference(FIREBASE_REF_ZONES).child(String.valueOf(eventId));
        return dbRef.setValue(zones).continueWith((v) -> {
            v.getResult(); // Forward exceptions
            return zones;
        });
    }

    @Override
    public Task<List<Spot>> updateSpots(int eventId, List<Spot> spots) {
        FirebaseDatabase fdB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fdB.getReference(FIREBASE_REF_SPOTS).child(String.valueOf(eventId));
        return dbRef.setValue(spots).continueWith((v) -> {
            v.getResult(); // Forward exceptions
            return spots;
        });
    }
}
