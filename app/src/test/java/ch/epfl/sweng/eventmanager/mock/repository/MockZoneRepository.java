package ch.epfl.sweng.eventmanager.mock.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

public class MockZoneRepository {
    private static final String TAG = "ZoneRepository";
    private List<Zone> zones = new ArrayList<>();

    public MockZoneRepository(){
        List<Position> positions1 = new ArrayList<>();
        positions1.add(new Position(0,0));
        positions1.add(new Position(2,2));
        Zone zone1 = new Zone(positions1);
        List<Position> positions2 = new ArrayList<>();
        positions2.add(new Position(0,0));
        positions2.add(new Position(2,2));
        Zone zone2 = new Zone(positions2);
        zones.add(zone1);
        zones.add(zone2);
    }

    public LiveData<List<Zone>> getZone(int eventId) {
        final MutableLiveData<List<Zone>> data = new MutableLiveData<>();
        data.postValue(zones);

        return data;
    }
}
