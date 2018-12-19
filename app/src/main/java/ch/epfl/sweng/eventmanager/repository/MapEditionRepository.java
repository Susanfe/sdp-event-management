package ch.epfl.sweng.eventmanager.repository;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

public interface MapEditionRepository {
    Task<Zone> updateZones(int eventId, Zone zone);

    Task<List<Spot>> updateSpots(int eventId, List<Spot> spots);
}
