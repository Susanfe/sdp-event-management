package ch.epfl.sweng.eventmanager.repository;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

public interface MapEditionRepository {
    Task<List<Zone>> updateZones(int eventId, List<Zone> zone);

    Task<List<Spot>> updateSpots(int eventId, List<Spot> spots);
}
