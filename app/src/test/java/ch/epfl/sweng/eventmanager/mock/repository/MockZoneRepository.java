package ch.epfl.sweng.eventmanager.mock.repository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sweng.eventmanager.repository.data.Position;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

public class MockZoneRepository {
    private static final String TAG = "ZoneRepository";
    private Zone zone;

    public MockZoneRepository(){
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0,0));
        positions.add(new Position(2,2));
        positions.add(new Position(1,2));
        positions.add(new Position(2,1));
        zone = new Zone(positions);
    }

    public LiveData<Zone> getZone(int eventId) {
        final MutableLiveData<Zone> data = new MutableLiveData<>();
        data.postValue(zone);

        return data;
    }
}
