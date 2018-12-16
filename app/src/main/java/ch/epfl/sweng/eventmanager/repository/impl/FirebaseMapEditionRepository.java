package ch.epfl.sweng.eventmanager.repository.impl;

import com.google.android.gms.tasks.Task;

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
    public FirebaseMapEditionRepository(){
    }

    @Override
    public Task<Void> updateZones(int eventId, List<Zone> zones) {
        return FirebaseHelper.replaceElement(eventId, FIREBASE_REF_ZONES, zones);
    }

    @Override
    public Task<Void> updateSpots(int eventId, List<Spot> spots) {
        return FirebaseHelper.replaceElement(eventId, FIREBASE_REF_SPOTS, spots);
    }
}
