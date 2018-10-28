package ch.epfl.sweng.eventmanager.ui.eventShowcase.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import ch.epfl.sweng.eventmanager.repository.EventRepository;
import ch.epfl.sweng.eventmanager.repository.data.Spot;
import ch.epfl.sweng.eventmanager.repository.data.Zone;

import javax.inject.Inject;
import java.util.List;

/**
 * This is the model for the zone list. It connects with the repository to pull a list of position and communicate them
 * to the view (here, the activity).
 *
 */
public class ZoneModel extends ViewModel {
    private LiveData<List<Zone>> zone;

    private EventRepository repository;

    private int eventId;

    @Inject
    public ZoneModel(EventRepository repository) {
        this.repository = repository;
    }

    public void init(int eventId) {
        if (this.zone != null) {
            return;
        }

        this.eventId = eventId;
        this.zone = repository.getZone(eventId);
    }

    public LiveData<List<Zone>> getZone() {
        return zone;
    }
}
