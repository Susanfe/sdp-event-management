package ch.epfl.sweng.eventmanager.repository;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

public interface MapEditionRepository {
    public Task<Void> updateZones(int eventId, List<Zone> zone);

    public Task<Void> updateSpots(int eventId, List<Spot> spots);
}
